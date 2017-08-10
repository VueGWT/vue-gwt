package com.axellience.vuegwt.jsr69;

import com.axellience.vuegwt.jsr69.component.ComponentWithTemplateGenerator;
import com.axellience.vuegwt.jsr69.component.template.TemplateBundleGenerator;
import com.axellience.vuegwt.jsr69.component.template.TemplateResourceGenerator;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.JsComponent;
import com.axellience.vuegwt.jsr69.component.factory.VueComponentFactoryGenerator;
import com.axellience.vuegwt.jsr69.component.factory.VueJsComponentFactoryGenerator;
import com.axellience.vuegwt.jsr69.directive.VueDirectiveOptionsGenerator;
import com.axellience.vuegwt.jsr69.directive.annotations.Directive;
import com.axellience.vuegwt.jsr69.style.StyleProviderGenerator;
import com.axellience.vuegwt.jsr69.style.annotations.Style;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.util.Set;

@SupportedAnnotationTypes({
    "com.axellience.vuegwt.jsr69.component.annotations.Component",
    "com.axellience.vuegwt.jsr69.component.annotations.JsComponent",
    "com.axellience.vuegwt.jsr69.directive.annotations.Directive",
    "com.axellience.vuegwt.jsr69.style.annotations.Style",
    "com.axellience.vuegwt.jsr69.inject.annotations.VueInjector"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class VueGwtProcessor extends AbstractProcessor
{
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv)
    {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
    {
        this.processStyleAnnotations(roundEnv);
        this.processDirectiveAnnotations(roundEnv);
        this.processComponentAnnotations(roundEnv);
        this.processJsComponentAnnotations(roundEnv);

        // claim the annotation
        return true;
    }

    private void processStyleAnnotations(RoundEnvironment roundEnv)
    {
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(Style.class);

        StyleProviderGenerator styleProviderGenerator = new StyleProviderGenerator(processingEnv);
        for (TypeElement element : ElementFilter.typesIn(annotatedElements))
        {
            styleProviderGenerator.generate(element);
        }
    }

    private void processDirectiveAnnotations(RoundEnvironment roundEnv)
    {
        Set<? extends Element> annotatedElements =
            roundEnv.getElementsAnnotatedWith(Directive.class);

        VueDirectiveOptionsGenerator vueDirectiveOptionsGenerator =
            new VueDirectiveOptionsGenerator(processingEnv);
        for (TypeElement element : ElementFilter.typesIn(annotatedElements))
        {
            vueDirectiveOptionsGenerator.generate(element);
        }
    }

    private void processComponentAnnotations(RoundEnvironment roundEnv)
    {
        Set<? extends Element> componentElements =
            roundEnv.getElementsAnnotatedWith(Component.class);

        TemplateResourceGenerator templateResourceGenerator =
            new TemplateResourceGenerator(processingEnv);
        ComponentWithTemplateGenerator componentWithTemplateGenerator =
            new ComponentWithTemplateGenerator(processingEnv);
        TemplateBundleGenerator templateBundleGenerator =
            new TemplateBundleGenerator(processingEnv);
        VueComponentFactoryGenerator vueFactoryGenerator =
            new VueComponentFactoryGenerator(processingEnv);

        for (TypeElement componentType : ElementFilter.typesIn(componentElements))
        {
            templateResourceGenerator.generate(componentType);
            vueFactoryGenerator.generate(componentType);
            templateBundleGenerator.generate(componentType);
            componentWithTemplateGenerator.generate(componentType);
        }
    }

    private void processJsComponentAnnotations(RoundEnvironment roundEnv)
    {
        Set<? extends Element> annotatedElements =
            roundEnv.getElementsAnnotatedWith(JsComponent.class);

        VueJsComponentFactoryGenerator vueJsComponentRegistrationGenerator =
            new VueJsComponentFactoryGenerator(processingEnv);

        for (TypeElement element : ElementFilter.typesIn(annotatedElements))
        {
            vueJsComponentRegistrationGenerator.generate(element);
        }
    }
}