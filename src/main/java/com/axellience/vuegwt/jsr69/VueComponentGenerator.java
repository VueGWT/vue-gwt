package com.axellience.vuegwt.jsr69;

import com.axellience.vuegwt.client.jsnative.JsArray;
import com.axellience.vuegwt.client.jsnative.Vue;
import com.axellience.vuegwt.client.jsnative.definitions.ComponentDefinition;
import com.axellience.vuegwt.client.jsnative.definitions.NamedVueProperty;
import com.axellience.vuegwt.jsr69.annotations.Component;
import com.axellience.vuegwt.jsr69.annotations.Computed;
import com.axellience.vuegwt.jsr69.annotations.Watch;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import jsinterop.annotations.JsType;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class VueComponentGenerator
{
    public static String COMPONENT_DEFINITION_SUFFIX = "_ComponentDefinition";
    public static String JCI                         = "javaComponentInstance";

    private final Messager messager;
    private final Elements elementsUtils;
    private final Types    typeUtils;
    private final Filer    filer;

    private final TypeElement componentTypeElement;

    private final String generatedTypeName;
    private final String typeName;
    private final String packageName;

    public VueComponentGenerator(ProcessingEnvironment processingEnv,
        TypeElement componentTypeElement)
    {
        messager = processingEnv.getMessager();
        elementsUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();

        this.componentTypeElement = componentTypeElement;

        this.packageName =
            elementsUtils.getPackageOf(componentTypeElement).getQualifiedName().toString();
        this.typeName = componentTypeElement.getSimpleName().toString();
        this.generatedTypeName = typeName + COMPONENT_DEFINITION_SUFFIX;
    }

    public void generate()
    {
        Builder componentClassBuilder = TypeSpec.classBuilder(generatedTypeName)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(ComponentDefinition.class)
            .addAnnotation(JsType.class)
            .addJavadoc("Vue Component for component {@link $S}",
                this.componentTypeElement.getQualifiedName().toString()
            );

        // Static init block
        componentClassBuilder.addStaticBlock(
            CodeBlock.of("$T.registerComponent($T.class, new $L());", Vue.class,
                TypeName.get(componentTypeElement.asType()), generatedTypeName
            ));

        // Template provider
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addStatement("this.$L = new $T()", JCI, TypeName.get(componentTypeElement.asType()))
            .addStatement("this.$L.setTemplate($T.INSTANCE.$L())", JCI, ClassName.get(packageName,
                typeName + TemplateProviderGenerator.TEMPLATE_PROVIDER_SUFFIX
            ), TemplateProviderGenerator.TEMPLATE_METHOD_NAME);

        // Add all properties as component data attributes
        constructorBuilder.addStatement("this.dataPropertiesNames = $T.of($L)", JsArray.class,
            ElementFilter.fieldsIn(componentTypeElement.getEnclosedElements())
                .stream()
                .map(VueComponentGenerator::surroundWithQuote)
                .collect(Collectors.joining(", "))
        );

        // Add all methods as component methods
        constructorBuilder.addStatement("this.methodsNames = $T.of($L)", JsArray.class,
            ElementFilter.methodsIn(componentTypeElement.getEnclosedElements())
                .stream()
                .filter(executableElement -> !hasAnnotation(executableElement, Computed.class))
                .map(VueComponentGenerator::surroundWithQuote)
                .collect(Collectors.joining(", "))
        );

        // Add all computed properties
        ElementFilter.methodsIn(componentTypeElement.getEnclosedElements())
            .stream()
            .filter(executableElement -> hasAnnotation(executableElement, Computed.class))
            .forEach(executableElement ->
            {
                String javaName = executableElement.getSimpleName().toString();
                String jsName = javaName;

                Computed computed = executableElement.getAnnotation(Computed.class);
                if (!"".equals(computed.name()))
                    jsName = computed.name();

                constructorBuilder.addStatement("this.computed.push(new $T($S, $S))",
                    NamedVueProperty.class, javaName, jsName
                );
            });

        // Add all watched properties
        ElementFilter.methodsIn(componentTypeElement.getEnclosedElements())
            .stream()
            .filter(executableElement -> hasAnnotation(executableElement, Watch.class))
            .forEach(executableElement ->
            {
                Watch watch = executableElement.getAnnotation(Watch.class);
                String javaName = executableElement.getSimpleName().toString();
                String jsName = watch.watchedProperty();

                constructorBuilder.addStatement("this.watched.push(new $T($S, $S))",
                    NamedVueProperty.class, javaName, jsName
                );
            });

        Component annotation = componentTypeElement.getAnnotation(Component.class);

        // Add components dependencies
        try
        {
            Class<?>[] componentsClass = annotation.components();
            Stream.of(componentsClass)
                .forEach(
                    clazz -> constructorBuilder.addStatement("this.$L.registerComponent($L.class)",
                        JCI, clazz.getCanonicalName()
                    ));
        }
        catch (MirroredTypesException mte)
        {
            List<DeclaredType> classTypeMirrors = (List<DeclaredType>) mte.getTypeMirrors();
            classTypeMirrors.forEach(classTypeMirror ->
            {
                TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
                constructorBuilder.addStatement("this.$L.registerComponent($L.class)", JCI,
                    classTypeElement.getQualifiedName().toString()
                );
            });
        }

        // Add props
        Stream.of(annotation.props())
            .forEach(prop -> constructorBuilder.addStatement("this.$L.addProp($S)", JCI, prop));

        // Create and add constructor
        componentClassBuilder.addMethod(constructorBuilder.build());

        TypeSpec componentClass = componentClassBuilder.build();

        try
        {
            JavaFile javaFile = JavaFile.builder(packageName, componentClass).build();

            JavaFileObject javaFileObject =
                filer.createSourceFile(getGeneratedFullQualifiedName(), componentTypeElement);

            Writer writer = javaFileObject.openWriter();
            javaFile.writeTo(writer);
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    static String surroundWithQuote(String string)
    {
        return "\"" + string + "\"";
    }

    static String surroundWithQuote(VariableElement variableElement)
    {
        return "\"" + variableElement.getSimpleName().toString() + "\"";
    }

    static String surroundWithQuote(ExecutableElement executableElement)
    {
        return "\"" + executableElement.getSimpleName().toString() + "\"";
    }

    private boolean hasAnnotation(Element element, Class<? extends Annotation> annotation)
    {
        return element.getAnnotation(annotation) != null;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public String getGeneratedTypeName()
    {
        return generatedTypeName;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public String getFullQualifiedName()
    {
        return packageName + "." + typeName;
    }

    public String getGeneratedFullQualifiedName()
    {
        return packageName + "." + generatedTypeName;
    }
}
