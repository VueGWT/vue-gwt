package com.axellience.vuegwt.processors.style;

import com.axellience.vuegwt.core.generation.GenerationUtil;
import com.squareup.javapoet.TypeName;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import static com.axellience.vuegwt.core.generation.GenerationNameUtil.STYLE_BUNDLE_METHOD_NAME;
import static com.axellience.vuegwt.core.generation.GenerationNameUtil.styleBundleName;

/**
 * Generate the Style Bundle for each Style
 * @author Adrien Baron
 */
public class StyleProviderGenerator
{
    private final Filer filer;
    private final Messager messager;

    public StyleProviderGenerator(ProcessingEnvironment processingEnvironment)
    {
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
    }

    public void generate(TypeElement styleTypeElement)
    {
        messager.printMessage(Kind.WARNING,
            "@Style annotation is deprecated and will be removed in Vue GWT 1.0-beta-6. Used on CssResource: "
                + styleTypeElement.getQualifiedName().toString());

        GenerationUtil.generateGwtBundle(styleTypeElement,
            styleBundleName(styleTypeElement),
            STYLE_BUNDLE_METHOD_NAME,
            TypeName.get(styleTypeElement.asType()),
            "gss",
            filer);
    }
}