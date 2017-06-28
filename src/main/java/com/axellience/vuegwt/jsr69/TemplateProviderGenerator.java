package com.axellience.vuegwt.jsr69;

import com.axellience.vuegwt.client.gwtextension.TemplateResource;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
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
    public static String TEMPLATE_PROVIDER_SUFFIX = "_TemplateProvider";
    public static String TEMPLATE_RESOURCE_SUFFIX = "_TemplateResource";
    public static String TEMPLATE_METHOD_NAME = "template";

    private final Filer filer;
    private final Elements elementsUtils;

    public TemplateProviderGenerator(ProcessingEnvironment processingEnvironment)
    {
        filer = processingEnvironment.getFiler();
        elementsUtils = processingEnvironment.getElementUtils();
    }

    public void generate(TypeElement componentTypeElement)
    {
        // Template provider
        String templateProviderPackage =
            elementsUtils.getPackageOf(componentTypeElement).getQualifiedName().toString();
        String templateProviderName =
            componentTypeElement.getSimpleName() + TEMPLATE_PROVIDER_SUFFIX;
        ClassName templateProviderClassName =
            ClassName.get(templateProviderPackage, templateProviderName);

        Builder templateClassBuilder = TypeSpec
            .interfaceBuilder(templateProviderName)
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(ClientBundle.class);

        templateClassBuilder.addField(FieldSpec
            .builder(templateProviderClassName,
                "INSTANCE",
                Modifier.PUBLIC,
                Modifier.STATIC,
                Modifier.FINAL)
            .initializer(CodeBlock.of("$T.create($T.class)", GWT.class, templateProviderClassName))
            .build());

        String typeElementName = componentTypeElement.getQualifiedName().toString();
        String vuePath = typeElementName.replaceAll("\\.", "/") + ".html";
        AnnotationSpec annotationSpec = AnnotationSpec
            .builder(Source.class)
            .addMember("value", CodeBlock.of("$S", vuePath))
            .build();

        templateClassBuilder.addMethod(MethodSpec
            .methodBuilder(TEMPLATE_METHOD_NAME)
            .addAnnotation(annotationSpec)
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(TemplateResource.class)
            .build());

        GenerationUtil.toJavaFile(filer,
            templateClassBuilder,
            templateProviderPackage,
            templateProviderName,
            componentTypeElement);

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
