package com.axellience.vuegwt.jsr69;

import com.axellience.vuegwt.client.gwtextension.TemplateResource;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;

/**
 * Generate the TemplateProvider for each Component
 */
public class TemplateProviderGenerator
{
    public static String TEMPLATE_PROVIDER_SUFFIX = "_TemplateProvider";
    public static String TEMPLATE_METHOD_NAME     = "template";

    private final Filer filer;
    private final Elements elementsUtils;

    public TemplateProviderGenerator(ProcessingEnvironment processingEnvironment)
    {
        filer = processingEnvironment.getFiler();
        elementsUtils = processingEnvironment.getElementUtils();
    }

    public TypeSpec generate(TypeElement componentTypeElement)
    {
        String templateProviderPackage =
            elementsUtils.getPackageOf(componentTypeElement).getQualifiedName().toString();
        String templateProviderName =
            componentTypeElement.getSimpleName() + TEMPLATE_PROVIDER_SUFFIX;
        ClassName templateProviderClassName = ClassName.get(templateProviderPackage, templateProviderName);

        Builder templateClassBuilder = TypeSpec.interfaceBuilder(templateProviderName)
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(ClientBundle.class);

        templateClassBuilder.addField(
            FieldSpec.builder(templateProviderClassName, "INSTANCE", Modifier.PUBLIC,
                Modifier.STATIC, Modifier.FINAL
            )
                .initializer(
                    CodeBlock.of("$T.create($T.class)", GWT.class, templateProviderClassName))
                .build());

        String typeElementName = componentTypeElement.getQualifiedName().toString();
        String vuePath = typeElementName.replaceAll("\\.", "/") + ".html";
        AnnotationSpec annotationSpec = AnnotationSpec.builder(Source.class)
            .addMember("value", CodeBlock.of("$S", vuePath))
            .build();

        templateClassBuilder.addMethod(MethodSpec.methodBuilder(TEMPLATE_METHOD_NAME)
            .addAnnotation(annotationSpec)
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(TemplateResource.class)
            .build());

        try
        {
            JavaFile javaFile = JavaFile.builder(templateProviderPackage, templateClassBuilder.build()).build();

            JavaFileObject javaFileObject =
                filer.createSourceFile(templateProviderPackage + "." + templateProviderName,
                    componentTypeElement
                );

            Writer writer = javaFileObject.openWriter();
            javaFile.writeTo(writer);
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return templateClassBuilder.build();
    }
}
