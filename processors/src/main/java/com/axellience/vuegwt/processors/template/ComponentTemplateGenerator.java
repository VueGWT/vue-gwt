package com.axellience.vuegwt.processors.template;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.client.template.ComponentTemplate;
import com.axellience.vuegwt.core.generation.GenerationUtil;
import com.google.gwt.resources.client.ClientBundle;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import static com.axellience.vuegwt.core.generation.GenerationNameUtil.componentTemplateName;

/**
 * Generate the GWT {@link ClientBundle} containing the template for our {@link VueComponent}.
 * @author Adrien Baron
 */
public class ComponentTemplateGenerator
{
    private final Filer filer;

    public ComponentTemplateGenerator(ProcessingEnvironment processingEnvironment)
    {
        filer = processingEnvironment.getFiler();
    }

    public void generate(TypeElement componentTypeElement)
    {
        Builder componentTemplateBuilder = TypeSpec
            .interfaceBuilder(componentTemplateName(componentTypeElement))
            .addSuperinterface(ParameterizedTypeName.get(ClassName.get(ComponentTemplate.class),
                ClassName.get(componentTypeElement)));

        GenerationUtil.toJavaFile(filer,
            componentTemplateBuilder,
            componentTemplateName(componentTypeElement),
            componentTypeElement);
    }
}
