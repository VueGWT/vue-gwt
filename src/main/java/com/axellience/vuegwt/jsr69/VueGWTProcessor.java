package com.axellience.vuegwt.jsr69;

import com.axellience.vuegwt.jsr69.annotations.Component;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.util.Set;

@SupportedAnnotationTypes({ "com.axellience.vuegwt.jsr69.annotations.Component" })
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class VueGWTProcessor extends AbstractProcessor
{
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
    {
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(
            Component.class);

        TemplateProviderGenerator templateProviderGenerator = new TemplateProviderGenerator(processingEnv);

        for (TypeElement element : ElementFilter.typesIn(annotatedElements))
        {
            VueComponentGenerator vueComponent = new VueComponentGenerator(processingEnv, element);
            vueComponent.generate();
            templateProviderGenerator.generate(element);
        }

        // claim the annotation
        return true;
    }
}
