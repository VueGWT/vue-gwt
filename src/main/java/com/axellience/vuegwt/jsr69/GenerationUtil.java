package com.axellience.vuegwt.jsr69;

import com.axellience.vuegwt.jsr69.component.annotations.Computed;
import com.google.gwt.core.ext.typeinfo.HasAnnotations;
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
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;
import java.beans.Introspector;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;

/**
 * @author Adrien Baron
 */
public class GenerationUtil
{
    public static boolean hasInterface(ProcessingEnvironment processingEnv, TypeMirror type,
        Class myInterface)
    {
        TypeMirror interfaceType =
            processingEnv.getElementUtils().getTypeElement(myInterface.getCanonicalName()).asType();

        return processingEnv.getTypeUtils().isAssignable(type, interfaceType);
    }

    public static String getComputedPropertyName(ExecutableElement method)
    {
        Computed computed = method.getAnnotation(Computed.class);
        String methodName = method.getSimpleName().toString();

        if (!"".equals(computed.propertyName()))
            return computed.propertyName();

        if (methodName.startsWith("get") || methodName.startsWith("set"))
            return Introspector.decapitalize(methodName.substring(3));

        if (methodName.startsWith("is"))
            return Introspector.decapitalize(methodName.substring(2));

        return methodName;
    }

    public static void toJavaFile(Filer filer, Builder classBuilder, ClassName className,
        TypeElement... originatingElement)
    {
        try
        {
            JavaFile javaFile =
                JavaFile.builder(className.packageName(), classBuilder.build()).build();

            JavaFileObject javaFileObject =
                filer.createSourceFile(className.reflectionName(), originatingElement);

            Writer writer = javaFileObject.openWriter();
            javaFile.writeTo(writer);
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void generateGwtBundle(TypeElement sourceType, ClassName bundleClassName,
        String bundleMethodName, TypeName resourceType, String resourceExtension, Filer filer)
    {
        Builder bundleClassBuilder = TypeSpec
            .interfaceBuilder(bundleClassName)
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(ClientBundle.class);

        bundleClassBuilder.addField(FieldSpec
            .builder(bundleClassName, "INSTANCE", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .initializer(CodeBlock.of("$T.create($T.class)", GWT.class, bundleClassName))
            .build());

        String typeElementName = sourceType.getQualifiedName().toString();
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

        GenerationUtil.toJavaFile(filer, bundleClassBuilder, bundleClassName, sourceType);
    }

    public static boolean hasAnnotation(Element element,
        Class<? extends Annotation> annotationClass)
    {
        return element.getAnnotation(annotationClass) != null;
    }

    public static boolean hasAnnotation(HasAnnotations element,
        Class<? extends Annotation> annotationClass)
    {
        return element.getAnnotation(annotationClass) != null;
    }

    public static AnnotationSpec getUnusableByJSAnnotation()
    {
        return AnnotationSpec
            .builder(SuppressWarnings.class)
            .addMember("value", "$S", "unusable-by-js")
            .build();
    }
}
