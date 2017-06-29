package com.axellience.vuegwt.jsr69;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;

/**
 * @author Adrien Baron
 */
public class GenerationUtil
{
    public static void toJavaFile(Filer filer, Builder classBuilder, String packageName,
        String className, TypeElement... originatingElement)
    {
        try
        {
            JavaFile javaFile = JavaFile.builder(packageName, classBuilder.build()).build();

            JavaFileObject javaFileObject =
                filer.createSourceFile(packageName + "." + className, originatingElement);

            Writer writer = javaFileObject.openWriter();
            javaFile.writeTo(writer);
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void generateGwtBundle(TypeElement typeElement, String bundleSuffix,
        String bundleMethodName, TypeName resourceType,
        String resourceExtension, Filer filer, Elements elementsUtils)
    {
        // Template provider
        String bundlePackage =
            elementsUtils.getPackageOf(typeElement).getQualifiedName().toString();
        String bundleName = typeElement.getSimpleName() + bundleSuffix;
        ClassName bundleClassName = ClassName.get(bundlePackage, bundleName);

        Builder bundleClassBuilder = TypeSpec
            .interfaceBuilder(bundleName)
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(ClientBundle.class);

        bundleClassBuilder.addField(FieldSpec
            .builder(bundleClassName, "INSTANCE", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .initializer(CodeBlock.of("$T.create($T.class)", GWT.class, bundleClassName))
            .build());

        String typeElementName = typeElement.getQualifiedName().toString();
        String resourcePath = typeElementName.replaceAll("\\.", "/") + "." + resourceExtension;
        AnnotationSpec annotationSpec = AnnotationSpec
            .builder(Source.class)
            .addMember("value", CodeBlock.of("$S", resourcePath))
            .build();

        bundleClassBuilder.addMethod(MethodSpec
            .methodBuilder(bundleMethodName)
            .addAnnotation(annotationSpec)
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(resourceType)
            .build());

        GenerationUtil.toJavaFile(filer,
            bundleClassBuilder,
            bundlePackage,
            bundleName,
            typeElement);
    }
}
