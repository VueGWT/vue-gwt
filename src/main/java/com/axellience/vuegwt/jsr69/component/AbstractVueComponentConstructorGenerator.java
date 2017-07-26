package com.axellience.vuegwt.jsr69.component;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.client.vue.VueConstructor;
import com.axellience.vuegwt.jsr69.GenerationUtil;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.JsComponent;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Abstract class to generate {@link VueConstructor} from the user Vue Component classes.
 * It is inherited by {@link VueComponentConstructorGenerator} which generate for {@link Component}
 * annotated class and {@link VueJsComponentConstructorGenerator} which generate for {@link
 * JsComponent} annotated class.
 * @author Adrien Baron
 */
public abstract class AbstractVueComponentConstructorGenerator
{
    static String CONSTRUCTOR_SUFFIX = "Constructor";
    static String INSTANCE_PROP = "vuegwt$constructor";
    static String DEPENDENCIES_INJECTED_PROP = "vuegwt$areDependenciesInjected";

    private final Elements elementsUtils;
    private final Filer filer;

    AbstractVueComponentConstructorGenerator(ProcessingEnvironment processingEnv)
    {
        this.elementsUtils = processingEnv.getElementUtils();
        this.filer = processingEnv.getFiler();
    }

    /**
     * Generate our {@link VueConstructor} class.
     * @param componentTypeElement The {@link VueComponent} class to
     * generate {@link VueComponentOptions} from
     */
    public void generate(TypeElement componentTypeElement)
    {
        String packageName =
            elementsUtils.getPackageOf(componentTypeElement).getQualifiedName().toString();
        String className = componentTypeElement.getSimpleName().toString();
        String generatedClassName = className + CONSTRUCTOR_SUFFIX;
        TypeName generatedTypeName = ClassName.get(packageName, generatedClassName);

        Builder vueConstructorClassBuilder =
            createConstructorBuilderClass(componentTypeElement, generatedClassName);

        createStaticRegistration(componentTypeElement,
            generatedTypeName,
            vueConstructorClassBuilder);

        createProperties(generatedTypeName, vueConstructorClassBuilder);

        createGetMethod(generatedTypeName, vueConstructorClassBuilder);

        createCreateInstanceMethod(componentTypeElement,
            packageName,
            className,
            generatedTypeName,
            vueConstructorClassBuilder);

        // Build the ComponentOptions class
        GenerationUtil.toJavaFile(filer,
            vueConstructorClassBuilder,
            packageName,
            generatedClassName,
            componentTypeElement);
    }

    protected abstract void createStaticRegistration(TypeElement componentTypeElement,
        TypeName generatedTypeName, Builder vueConstructorClassBuilder);

    /**
     * Create the builder to build our {@link VueConstructor} class.
     * @param componentTypeElement The Component we generate for
     * @param generatedClassName The type name of our generated {@link VueConstructor}
     * @return A Class Builder
     */
    private Builder createConstructorBuilderClass(TypeElement componentTypeElement,
        String generatedClassName)
    {
        return TypeSpec
            .classBuilder(generatedClassName)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(ParameterizedTypeName.get(ClassName.get(VueConstructor.class),
                ClassName.get(componentTypeElement)))
            .addAnnotation(AnnotationSpec
                .builder(JsType.class)
                .addMember("isNative", "true", true)
                .addMember("namespace", "\"" + JsPackage.GLOBAL + "\"")
                .addMember("name", "\"Function\"")
                .build())
            .addJavadoc("VueConstructor for Component {@link $S}",
                componentTypeElement.getQualifiedName().toString());
    }

    /**
     * Create the createInstance method. This method will return a new instance of our generated
     * {@link VueConstructor} while making sure dependencies are injected.
     * @param componentTypeElement The Component we generate for
     * @param packageName The name of the package this Component is in
     * @param className The name of our Component class
     * @param generatedTypeName The generated type name for our {@link VueConstructor}
     * @param vueConstructorClassBuilder The builder to create our {@link VueConstructor} class
     */
    protected abstract void createCreateInstanceMethod(TypeElement componentTypeElement,
        String packageName, String className, TypeName generatedTypeName,
        Builder vueConstructorClassBuilder);

    /**
     * Create the static properties used in our {@link VueConstructor}.
     * @param generatedTypeName
     * @param vueConstructorClassBuilder
     */
    private void createProperties(TypeName generatedTypeName, Builder vueConstructorClassBuilder)
    {
        vueConstructorClassBuilder.addField(FieldSpec
            .builder(generatedTypeName, INSTANCE_PROP, Modifier.PRIVATE, Modifier.STATIC)
            .addAnnotation(JsOverlay.class)
            .build());

        vueConstructorClassBuilder.addField(FieldSpec
            .builder(TypeName.BOOLEAN,
                DEPENDENCIES_INJECTED_PROP,
                Modifier.PRIVATE,
                Modifier.STATIC)
            .addAnnotation(JsOverlay.class)
            .build());
    }

    private void createGetMethod(TypeName generatedTypeName, Builder vueConstructorClassBuilder)
    {
        MethodSpec.Builder getBuilder = MethodSpec
            .methodBuilder("get")
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .addAnnotation(JsOverlay.class)
            .returns(generatedTypeName)
            .beginControlFlow("if ($L == null)", INSTANCE_PROP)
            .addStatement("$L = createInstance()", INSTANCE_PROP)
            .endControlFlow()
            .addStatement("return $L", INSTANCE_PROP);

        vueConstructorClassBuilder.addMethod(getBuilder.build());
    }
}
