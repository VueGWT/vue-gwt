package com.axellience.vuegwt.jsr69.component;

import com.axellience.vuegwt.client.template.TemplateResource;
import com.axellience.vuegwt.jsr69.GenerationUtil;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Generate the TemplateProvider for each Component
 * @author Adrien Baron
 */
public class TemplateProviderGenerator
{
    public static String TEMPLATE_BUNDLE_SUFFIX = "_TemplateBundle";
    public static String TEMPLATE_BUNDLE_METHOD_NAME = "template";
    public static String TEMPLATE_RESOURCE_SUFFIX = "_TemplateResource";

    private final Filer filer;
    private final Elements elementsUtils;

    public TemplateProviderGenerator(ProcessingEnvironment processingEnvironment)
    {
        filer = processingEnvironment.getFiler();
        elementsUtils = processingEnvironment.getElementUtils();
    }

    public void generate(TypeElement componentTypeElement)
    {
        GenerationUtil.generateGwtBundle(componentTypeElement,
            TEMPLATE_BUNDLE_SUFFIX,
            TEMPLATE_BUNDLE_METHOD_NAME,
            TypeName.get(TemplateResource.class),
            "html",
            filer,
            elementsUtils);

        // Template resource abstract class
        String templateResourcePackage =
            elementsUtils.getPackageOf(componentTypeElement).getQualifiedName().toString();
        String templateResourceName =
            componentTypeElement.getSimpleName() + TEMPLATE_RESOURCE_SUFFIX;

        Builder templateResourceClassBuilder = TypeSpec
            .classBuilder(templateResourceName)
            .addModifiers(Modifier.PUBLIC)
            .addModifiers(Modifier.ABSTRACT)
            .superclass(TypeName.get(componentTypeElement.asType()))
            .addSuperinterface(TemplateResource.class);

        GenerationUtil.toJavaFile(filer,
            templateResourceClassBuilder,
            templateResourcePackage,
            templateResourceName,
            componentTypeElement);
    }
}
