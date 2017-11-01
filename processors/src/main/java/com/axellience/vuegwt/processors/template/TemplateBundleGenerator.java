package com.axellience.vuegwt.processors.template;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.generation.GenerationUtil;
import com.axellience.vuegwt.gwt2.client.template.VueComponentTemplateResource;
import com.google.gwt.resources.client.ClientBundle;
import com.squareup.javapoet.TypeName;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import static com.axellience.vuegwt.core.generation.GenerationNameUtil.COMPONENT_TEMPLATE_BUNDLE_METHOD_NAME;
import static com.axellience.vuegwt.core.generation.GenerationNameUtil.componentTemplateBundleName;

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
            TypeName.get(VueComponentTemplateResource.class),
            "html",
            filer);
    }
}
