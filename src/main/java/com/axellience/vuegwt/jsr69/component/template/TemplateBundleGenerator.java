package com.axellience.vuegwt.jsr69.component.template;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.template.TemplateResource;
import com.axellience.vuegwt.jsr69.GenerationUtil;
import com.google.gwt.resources.client.ClientBundle;
import com.squareup.javapoet.TypeName;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import static com.axellience.vuegwt.jsr69.GenerationNameUtil.COMPONENT_TEMPLATE_BUNDLE_METHOD_NAME;
import static com.axellience.vuegwt.jsr69.GenerationNameUtil.componentTemplateBundleName;

/**
 * Generate the GWT {@link ClientBundle} containing the template for our {@link VueComponent}.
 * @author Adrien Baron
 */
public class TemplateBundleGenerator
{
    private final Filer filer;

    public TemplateBundleGenerator(ProcessingEnvironment processingEnvironment)
    {
        filer = processingEnvironment.getFiler();
    }

    public void generate(TypeElement componentTypeElement)
    {
        GenerationUtil.generateGwtBundle(componentTypeElement,
            componentTemplateBundleName(componentTypeElement),
            COMPONENT_TEMPLATE_BUNDLE_METHOD_NAME,
            TypeName.get(TemplateResource.class),
            "html",
            filer);
    }
}
