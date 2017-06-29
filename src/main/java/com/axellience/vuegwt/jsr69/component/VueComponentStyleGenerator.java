package com.axellience.vuegwt.jsr69.component;

import com.axellience.vuegwt.client.definitions.VueComponentStyle;
import com.axellience.vuegwt.jsr69.GenerationUtil;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.style.StyleProviderGenerator;
import com.axellience.vuegwt.jsr69.style.VueStyleInterfaceGenerator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.util.Elements;
import java.util.List;
import java.util.stream.Stream;

/**
 * Generate VueComponentStyle from the user VueComponent classes
 * This will inherit from all the VueStyleInterface generated for all the styles
 * this Component uses.
 * @author Adrien Baron
 */
public class VueComponentStyleGenerator
{
    public static String COMPONENT_STYLE_SUFFIX = "_ComponentStyle";

    private final Elements elementsUtils;
    private final Filer filer;

    public VueComponentStyleGenerator(ProcessingEnvironment processingEnv)
    {
        elementsUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
    }

    /**
     * Generate and save the Java file for the typeElement passed to the constructor
     */
    public void generate(TypeElement componentTypeElement)
    {
        String packageName =
            elementsUtils.getPackageOf(componentTypeElement).getQualifiedName().toString();
        String typeName = componentTypeElement.getSimpleName().toString();
        String generatedTypeName = typeName + COMPONENT_STYLE_SUFFIX;

        Builder componentStyleBuilder =
            this.getComponentStyleBuilder(componentTypeElement, generatedTypeName);

        MethodSpec.Builder constructorBuilder =
            MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);

        try
        {
            Component annotation = componentTypeElement.getAnnotation(Component.class);
            Class<?>[] stylesClass = annotation.styles();
            Stream
                .of(stylesClass)
                .forEach(aClass -> this.addStyle(packageName,
                    TypeName.get(aClass),
                    componentStyleBuilder,
                    constructorBuilder));
        }
        catch (MirroredTypesException mte)
        {
            List<DeclaredType> classTypeMirrors = (List<DeclaredType>) mte.getTypeMirrors();
            classTypeMirrors
                .stream()
                .map(DeclaredType::asElement)
                .map(TypeElement.class::cast)
                .forEach(typeElement -> this.addStyle(packageName,
                    TypeName.get(typeElement.asType()),
                    componentStyleBuilder,
                    constructorBuilder));
        }

        componentStyleBuilder.addMethod(constructorBuilder.build());

        // Build the component definition class
        GenerationUtil.toJavaFile(filer,
            componentStyleBuilder,
            packageName,
            generatedTypeName,
            componentTypeElement);
    }

    private Builder getComponentStyleBuilder(TypeElement componentTypeElement,
        String generatedTypeName)
    {
        return TypeSpec
            .classBuilder(generatedTypeName)
            .addModifiers(Modifier.PUBLIC)
            .superclass(VueComponentStyle.class)
            .addJavadoc("Vue Component Style for component {@link $S}",
                componentTypeElement.getQualifiedName().toString());
    }

    private void addStyle(String packageName, TypeName styleTypeName, Builder componentStyleBuilder,
        MethodSpec.Builder constructorBuilder)
    {
        ClassName styleBundleClassName =
            ClassName.get(packageName, styleTypeName + StyleProviderGenerator.STYLE_BUNDLE_SUFFIX);
        this.ensureInjectedStyle(styleBundleClassName, constructorBuilder);

        ClassName styleDefinitionClassName = ClassName.get(packageName,
            styleTypeName + VueStyleInterfaceGenerator.STYLE_DEFINITION_SUFFIX);
        componentStyleBuilder.addSuperinterface(styleDefinitionClassName);
    }

    private void ensureInjectedStyle(ClassName styleBundleClassName,
        MethodSpec.Builder constructorBuilder)
    {
        constructorBuilder.addStatement("$T.INSTANCE.$L().ensureInjected()",
            styleBundleClassName,
            StyleProviderGenerator.STYLE_BUNDLE_METHOD_NAME);
    }
}
