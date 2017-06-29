package com.axellience.vuegwt.jsr69.style;

import com.axellience.vuegwt.jsr69.GenerationUtil;
import com.squareup.javapoet.TypeName;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Generate the Style Bundle for each Style
 * @author Adrien Baron
 */
public class StyleProviderGenerator
{
    public static String STYLE_BUNDLE_SUFFIX = "_StyleBundle";
    public static String STYLE_BUNDLE_METHOD_NAME = "style";

    private final Filer filer;
    private final Elements elementsUtils;

    public StyleProviderGenerator(ProcessingEnvironment processingEnvironment)
    {
        filer = processingEnvironment.getFiler();
        elementsUtils = processingEnvironment.getElementUtils();
    }

    public void generate(TypeElement styleTypeElement)
    {
        GenerationUtil.generateGwtBundle(styleTypeElement,
            STYLE_BUNDLE_SUFFIX,
            STYLE_BUNDLE_METHOD_NAME,
            TypeName.get(styleTypeElement.asType()),
            "gss",
            filer,
            elementsUtils);
    }
}