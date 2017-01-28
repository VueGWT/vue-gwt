package com.axellience.vuegwt.jsr69;

import com.axellience.vuegwt.client.jsnative.Vue;
import com.axellience.vuegwt.client.definitions.ComponentDefinition;
import com.axellience.vuegwt.client.definitions.component.DataDefinition;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class VueComponentGenerator
{
    public static String COMPONENT_DEFINITION_SUFFIX = "_ComponentDefinition";
    public static String JCI                         = "javaComponentInstance";

    public static Map<String, Boolean> LIFECYCLE_HOOKS_MAP = new HashMap<>();

    static
    {
        LIFECYCLE_HOOKS_MAP.put("beforeCreate", true);
        LIFECYCLE_HOOKS_MAP.put("created", true);
        LIFECYCLE_HOOKS_MAP.put("beforeMount", true);
        LIFECYCLE_HOOKS_MAP.put("mounted", true);
        LIFECYCLE_HOOKS_MAP.put("beforeUpdate", true);
        LIFECYCLE_HOOKS_MAP.put("updated", true);
        LIFECYCLE_HOOKS_MAP.put("activated", true);
        LIFECYCLE_HOOKS_MAP.put("deactivated", true);
        LIFECYCLE_HOOKS_MAP.put("beforeDestroy", true);
        LIFECYCLE_HOOKS_MAP.put("destroyed", true);
    }

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
        Component annotation = componentTypeElement.getAnnotation(Component.class);

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

        // Initialize constructor
        MethodSpec.Builder constructorBuilder =
            MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);

        // Add the Java Component Instance initialization
        constructorBuilder.addStatement(
            "this.$L = new $T()", JCI, TypeName.get(componentTypeElement.asType()));

        // Add template initialization
        constructorBuilder.addStatement(
            "this.setTemplate($T.INSTANCE.$L().getText())", ClassName.get(packageName,
                typeName + TemplateProviderGenerator.TEMPLATE_PROVIDER_SUFFIX
            ), TemplateProviderGenerator.TEMPLATE_METHOD_NAME);

        // Data
        constructorBuilder.addStatement(
            "$T<$T> dataFields = new $T()", List.class, DataDefinition.class, LinkedList.class);
        ElementFilter.fieldsIn(componentTypeElement.getEnclosedElements())
            .forEach(
                variableElement -> constructorBuilder.addStatement("dataFields.add(new $T($S))",
                    DataDefinition.class, variableElement.getSimpleName()
                ));
        constructorBuilder.addStatement("this.initData(dataFields, $L)", annotation.useFactory());

        // Props
        Stream.of(annotation.props())
            .forEach(prop -> constructorBuilder.addStatement("this.addProp($S)", prop));

        // Methods
        ElementFilter.methodsIn(componentTypeElement.getEnclosedElements())
            .forEach(executableElement ->
            {
                String javaName = executableElement.getSimpleName().toString();
                Computed computed = executableElement.getAnnotation(Computed.class);
                Watch watch = executableElement.getAnnotation(Watch.class);

                if (computed != null)
                {
                    String jsName = !"".equals(computed.name()) ? computed.name() : javaName;
                    constructorBuilder.addStatement("this.addComputed($S, $S)", javaName, jsName);
                }
                else if (watch != null)
                {
                    String jsName = watch.watchedProperty();
                    constructorBuilder.addStatement("this.addWatch($S, $S)", javaName, jsName);
                }
                else if (LIFECYCLE_HOOKS_MAP.containsKey(javaName))
                {
                    constructorBuilder.addStatement("this.addLifecycleHook($S)", javaName);
                }
                else
                {
                    constructorBuilder.addStatement("this.addMethod($S)", javaName);
                }
            });

        // Components
        try
        {
            Class<?>[] componentsClass = annotation.components();
            Stream.of(componentsClass)
                .forEach(clazz -> constructorBuilder.addStatement("this.addComponent($L.class)",
                    clazz.getCanonicalName()
                ));
        }
        catch (MirroredTypesException mte)
        {
            List<DeclaredType> classTypeMirrors = (List<DeclaredType>) mte.getTypeMirrors();
            classTypeMirrors.forEach(classTypeMirror ->
            {
                TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
                constructorBuilder.addStatement("this.addComponent($L.class)",
                    classTypeElement.getQualifiedName().toString()
                );
            });
        }

        // Finish building the constructor and add to the component definition
        componentClassBuilder.addMethod(constructorBuilder.build());

        // Build the component definition class
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
