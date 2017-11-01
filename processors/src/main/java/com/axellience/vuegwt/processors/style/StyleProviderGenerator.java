package com.axellience.vuegwt.processors.style;

import com.axellience.vuegwt.core.generation.GenerationUtil;
import com.squareup.javapoet.TypeName;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import static com.axellience.vuegwt.core.generation.GenerationNameUtil.STYLE_BUNDLE_METHOD_NAME;
import static com.axellience.vuegwt.core.generation.GenerationNameUtil.styleBundleName;

/**
 * Generate the Style Bundle for each Style
 * @author Adrien Baron
 */
public class StyleProviderGenerator
{
    private final Filer filer;

    public StyleProviderGenerator(ProcessingEnvironment processingEnvironment)
    {
        filer = processingEnvironment.getFiler();
    }

    public void generate(TypeElement styleTypeElement)
    {
        GenerationUtil.generateGwtBundle(styleTypeElement,
            styleBundleName(styleTypeElement),
            STYLE_BUNDLE_METHOD_NAME,
            TypeName.get(styleTypeElement.asType()),
            "gss",
            filer);
    }
}