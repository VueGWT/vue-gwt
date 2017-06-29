package com.axellience.vuegwt.jsr69.style;

import com.axellience.vuegwt.jsr69.GenerationUtil;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;

/**
 * Generate a VueStyleInterface from @Style CssResources
 * Those interface have default methods returning the values of the Style.
 * VueComponent using a Style will have a @{@link com.axellience.vuegwt.client.definitions.VueComponentStyle}
 * implementing those interfaces.
 * @author Adrien Baron
 */
public class VueStyleInterfaceGenerator
{
    public static String STYLE_DEFINITION_SUFFIX = "_StyleInterface";

    private final Elements elementsUtils;
    private final Filer filer;

    public VueStyleInterfaceGenerator(ProcessingEnvironment processingEnv)
    {
        elementsUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
    }

    /**
     * Generate and save the Java file for the typeElement passed to the constructor
     */
    public void generate(TypeElement styleTypeElement)
    {
        String packageName = getPackageName(styleTypeElement);
        String typeName = styleTypeElement.getSimpleName().toString();
        String generatedTypeName = typeName + STYLE_DEFINITION_SUFFIX;

        Builder styleInterfaceBuilder =
            this.createStyleInterfaceBuilder(styleTypeElement, generatedTypeName);
        createStyleRulesMethod(styleTypeElement, styleInterfaceBuilder);

        GenerationUtil.toJavaFile(filer,
            styleInterfaceBuilder,
            packageName,
            generatedTypeName,
            styleTypeElement);
    }

    private Builder createStyleInterfaceBuilder(TypeElement styleTypeElement,
        String generatedTypeName)
    {
        return TypeSpec
            .interfaceBuilder(generatedTypeName)
            .addModifiers(Modifier.PUBLIC)
            .addJavadoc("Vue Style Definition for Style {@link $S}",
                styleTypeElement.getQualifiedName().toString());
    }

    private void createStyleRulesMethod(TypeElement styleTypeElement, Builder styleInterfaceBuilder)
    {
        String packageName = getPackageName(styleTypeElement);
        String typeName = styleTypeElement.getSimpleName().toString();
        ClassName styleBundleClassName =
            ClassName.get(packageName, typeName + StyleProviderGenerator.STYLE_BUNDLE_SUFFIX);

        ElementFilter
            .methodsIn(styleTypeElement.getEnclosedElements())
            .stream()
            .map(method -> createMethodSpecForStyleRule(method, styleBundleClassName))
            .forEach(styleInterfaceBuilder::addMethod);
    }

    private MethodSpec createMethodSpecForStyleRule(ExecutableElement method,
        ClassName styleBundleClassName)
    {
        return MethodSpec
            .methodBuilder(method.getSimpleName().toString())
            .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
            .returns(String.class)
            .addStatement("return $T.INSTANCE.$L().$L()",
                styleBundleClassName,
                StyleProviderGenerator.STYLE_BUNDLE_METHOD_NAME,
                method.getSimpleName().toString())
            .build();
    }

    private String getPackageName(TypeElement styleTypeElement)
    {
        return elementsUtils.getPackageOf(styleTypeElement).getQualifiedName().toString();
    }
}
