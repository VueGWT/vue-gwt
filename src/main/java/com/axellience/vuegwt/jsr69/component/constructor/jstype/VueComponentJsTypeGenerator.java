package com.axellience.vuegwt.jsr69.component.constructor.jstype;

import com.axellience.vuegwt.client.VueGWT;
import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.GenerationUtil;
import com.axellience.vuegwt.jsr69.component.annotations.Computed;
import com.axellience.vuegwt.jsr69.component.annotations.PropValidator;
import com.axellience.vuegwt.jsr69.component.annotations.Watch;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import jsinterop.annotations.JsType;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import java.util.List;
import java.util.stream.Collectors;

import static com.axellience.vuegwt.jsr69.component.constructor.AbstractVueComponentConstructorGenerator.CONSTRUCTOR_SUFFIX;

/**
 * Generate a class extending our {@link VueComponent} class.
 * This will allow us to expose it's constructor and methods to JS.
 * @author Adrien Baron
 */
public class VueComponentJsTypeGenerator
{
    private static final String JS_TYPE_SUFFIX = "JsType";
    private final Elements elementsUtils;
    private final Filer filer;

    public VueComponentJsTypeGenerator(ProcessingEnvironment processingEnv)
    {
        this.elementsUtils = processingEnv.getElementUtils();
        this.filer = processingEnv.getFiler();
    }

    /**
     * Generate a class extending our {@link VueComponent} class.
     * This will allow us to expose it's constructor and methods to JS.
     * @param componentTypeElement The {@link VueComponent} to extend
     */
    public void generate(TypeElement componentTypeElement)
    {
        String packageName =
            elementsUtils.getPackageOf(componentTypeElement).getQualifiedName().toString();
        String className = componentTypeElement.getSimpleName().toString();
        String generatedClassName = className + JS_TYPE_SUFFIX;

        Builder vueComponentJsTypeClassBuilder = TypeSpec
            .classBuilder(generatedClassName)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(ClassName.get(componentTypeElement))
            .addAnnotation(AnnotationSpec
                .builder(JsType.class)
                .addMember("namespace", "\"VueGWT.javaComponentConstructors\"")
                .addMember("name",
                    "\""
                        + componentTypeElement.getQualifiedName().toString().replaceAll("\\.", "_")
                        + "\"")
                .build());

        createConstructorStaticRegistration(packageName, className, vueComponentJsTypeClassBuilder);

        generateProxyConstructor(componentTypeElement, vueComponentJsTypeClassBuilder);
        generateProxyMethods(componentTypeElement, vueComponentJsTypeClassBuilder);

        GenerationUtil.toJavaFile(filer,
            vueComponentJsTypeClassBuilder,
            packageName,
            generatedClassName,
            componentTypeElement);
    }

    /**
     * Register the constructor in VueGWT.
     * This is done here because JsType class will always be included in GWT output.
     * The Constructor is not @JsType.
     * @param packageName The VueComponent Package
     * @param className The VueComponent Class Name
     * @param vueComponentJsTypeClassBuilder The builder for our VueComponentJsType class
     */
    protected void createConstructorStaticRegistration(String packageName, String className,
        Builder vueComponentJsTypeClassBuilder)
    {
        vueComponentJsTypeClassBuilder.addStaticBlock(CodeBlock
            .builder()
            .addStatement("$T.onReady(() -> $T.register($S, $T.get()))",
                VueGWT.class,
                VueGWT.class,
                packageName + "." + className,
                ClassName.bestGuess(packageName + "." + className + CONSTRUCTOR_SUFFIX))
            .build());
    }

    /**
     * Generate a JsType proxy for our Component Constructor.
     * This Constructor will be called upon Vue Component instantiation by Vue.
     * If our Component doesn't have a constructor just create an empty one.
     * Throw an exception if our Component has several constructors.
     * @param componentTypeElement Component to process
     * @param vueComponentJsTypeClassBuilder The builder for our VueComponentJsType class
     */
    private void generateProxyConstructor(TypeElement componentTypeElement,
        Builder vueComponentJsTypeClassBuilder)
    {
        List<ExecutableElement> constructors =
            ElementFilter.constructorsIn(componentTypeElement.getEnclosedElements());

        if (constructors.isEmpty())
        {
            vueComponentJsTypeClassBuilder.addMethod(MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .build());
            return;
        }

        if (constructors.size() > 1)
        {
            throw new RuntimeException("VueComponent should not have more than one constructor.");
        }

        ExecutableElement componentConstructor = constructors.get(0);
        MethodSpec.Builder proxyConstructorBuilder =
            MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);

        addProxyParameters(proxyConstructorBuilder, componentConstructor);
        proxyConstructorBuilder.addStatement("super($L)",
            getSuperMethodCallParameters(componentConstructor));

        vueComponentJsTypeClassBuilder.addMethod(proxyConstructorBuilder.build());
    }

    /**
     * Generate JsType proxy for all of the Component methods.
     * These proxy will keep the same name in JS and can be therefore passed to Vue to
     * configure our Component.
     * @param componentTypeElement Component to process
     * @param vueComponentJsTypeClassBuilder The builder for our VueComponentJsType class
     */
    private void generateProxyMethods(TypeElement componentTypeElement,
        Builder vueComponentJsTypeClassBuilder)
    {
        ElementFilter
            .methodsIn(componentTypeElement.getEnclosedElements())
            .forEach(executableElement -> {
                Computed computed = executableElement.getAnnotation(Computed.class);
                Watch watch = executableElement.getAnnotation(Watch.class);
                PropValidator propValidator = executableElement.getAnnotation(PropValidator.class);

                if (computed != null || watch != null || propValidator != null)
                {
                    generateProxyMethod(vueComponentJsTypeClassBuilder, executableElement);
                }
            });
    }

    /**
     * Generate a JsType proxy for Component method.
     * This proxy will keep the same name in JS and can be therefore passed to Vue to
     * configure our Component.
     * @param vueComponentJsTypeClassBuilder The builder for our VueComponentJsType class
     * @param originalMethod Method to proxify
     */
    private void generateProxyMethod(Builder vueComponentJsTypeClassBuilder,
        ExecutableElement originalMethod)
    {
        MethodSpec.Builder proxyMethodBuilder = MethodSpec
            .methodBuilder(originalMethod.getSimpleName().toString())
            .addModifiers(Modifier.PUBLIC)
            .returns(ClassName.get(originalMethod.getReturnType()));

        addProxyParameters(proxyMethodBuilder, originalMethod);

        String methodCallParameters = getSuperMethodCallParameters(originalMethod);

        String returnStatement = "";
        if (!"void".equals(originalMethod.getReturnType().toString()))
            returnStatement = "return ";

        proxyMethodBuilder.addStatement(returnStatement + "super.$L($L)",
            originalMethod.getSimpleName().toString(),
            methodCallParameters);

        vueComponentJsTypeClassBuilder.addMethod(proxyMethodBuilder.build());
    }

    /**
     * Copy parameters from an original method to a proxy method.
     * @param proxyMethodBuilder A Builder for the proxy method
     * @param sourceMethod The source method to copy from
     */
    private void addProxyParameters(MethodSpec.Builder proxyMethodBuilder,
        ExecutableElement sourceMethod)
    {
        sourceMethod
            .getParameters()
            .forEach(parameter -> proxyMethodBuilder.addParameter(TypeName.get(parameter.asType()),
                parameter.getSimpleName().toString()));
    }

    /**
     * Return the list of parameters name to pass to the super call on proxy methods.
     * @param sourceMethod The source method
     * @return A string which is the list of parameters name joined by ", "
     */
    private String getSuperMethodCallParameters(ExecutableElement sourceMethod)
    {
        return sourceMethod
            .getParameters()
            .stream()
            .map(parameter -> parameter.getSimpleName().toString())
            .collect(Collectors.joining(", "));
    }
}
