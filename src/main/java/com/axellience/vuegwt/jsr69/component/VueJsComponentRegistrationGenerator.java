package com.axellience.vuegwt.jsr69.component;

import com.axellience.vuegwt.client.Vue;
import com.axellience.vuegwt.client.VueGwtCache;
import com.axellience.vuegwt.client.tools.JsTools;
import com.axellience.vuegwt.jsr69.GenerationUtil;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Generate VueComponentOptions from the user VueComponent classes
 * @author Adrien Baron
 */
public class VueJsComponentRegistrationGenerator
{
    private static String REGISTRATION_SUFFIX = "_Registration";

    private final Elements elementsUtils;
    private final Filer filer;

    public VueJsComponentRegistrationGenerator(ProcessingEnvironment processingEnv)
    {
        this.elementsUtils = processingEnv.getElementUtils();
        this.filer = processingEnv.getFiler();
    }

    /**
     * Generate a registration class for our Js Component
     * @param jsComponentTypeElement The {@link Vue} class to generate our registration from
     */
    public void generate(TypeElement jsComponentTypeElement)
    {
        String packageName =
            elementsUtils.getPackageOf(jsComponentTypeElement).getQualifiedName().toString();
        String typeName = jsComponentTypeElement.getSimpleName().toString();
        String generatedTypeName = typeName + REGISTRATION_SUFFIX;

        JsType jsType = jsComponentTypeElement.getAnnotation(JsType.class);
        if (jsType == null || !jsType.isNative())
            throw new RuntimeException(jsComponentTypeElement.asType().toString()
                + " Js Component must have @JsType annotation with isNative to true.");

        Builder componentClassBuilder = TypeSpec
            .classBuilder(generatedTypeName)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addAnnotation(JsType.class)
            .addJavadoc("Vue Js Component Registration for Js Component {@link $S}",
                jsComponentTypeElement.getQualifiedName().toString());

        String namespace = jsType.namespace();
        if (JsPackage.GLOBAL.equals(namespace))
            namespace = "";
        else if ("<auto>".equals(namespace))
            namespace = packageName + ".";
        else
            namespace += ".";

        String name = jsType.name();
        if ("<auto>".equals(name))
            name = jsComponentTypeElement.getSimpleName().toString();

        // Static init block
        componentClassBuilder.addStaticBlock(CodeBlock.of(
            "$T.registerJsComponent($T.class, $T.getDeepValue($T.getWindow(), $S));",
            VueGwtCache.class,
            TypeName.get(jsComponentTypeElement.asType()),
            JsTools.class,
            JsTools.class,
            namespace + name));

        // Build the ComponentOptions class
        GenerationUtil.toJavaFile(filer,
            componentClassBuilder,
            packageName,
            generatedTypeName,
            jsComponentTypeElement);
    }
}
