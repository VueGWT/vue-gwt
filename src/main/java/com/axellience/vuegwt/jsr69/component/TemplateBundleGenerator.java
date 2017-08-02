package com.axellience.vuegwt.jsr69.component;

import com.axellience.vuegwt.client.component.template.ComponentWithTemplate;
import com.axellience.vuegwt.jsr69.GenerationUtil;
import com.squareup.javapoet.TypeName;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * @author Adrien Baron
 */
public class TemplateBundleGenerator
{
    public static String TEMPLATE_BUNDLE_SUFFIX = "TemplateBundle";
    public static String TEMPLATE_BUNDLE_METHOD_NAME = "template";

    private final Filer filer;
    private final Elements elementsUtils;

    public TemplateBundleGenerator(ProcessingEnvironment processingEnvironment)
    {
        filer = processingEnvironment.getFiler();
        elementsUtils = processingEnvironment.getElementUtils();
    }

    public void generate(TypeElement componentTypeElement)
    {
        GenerationUtil.generateGwtBundle(componentTypeElement,
            TEMPLATE_BUNDLE_SUFFIX,
            TEMPLATE_BUNDLE_METHOD_NAME,
            TypeName.get(ComponentWithTemplate.class),
            "html",
            filer,
            elementsUtils);
    }
}
