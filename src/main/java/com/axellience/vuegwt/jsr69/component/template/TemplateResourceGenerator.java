package com.axellience.vuegwt.jsr69.component.template;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.template.TemplateResource;
import com.axellience.vuegwt.jsr69.GenerationUtil;
import com.axellience.vuegwt.jsr69.component.ComponentGenerationUtil;
import com.axellience.vuegwt.jsr69.component.annotations.Computed;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.axellience.vuegwt.jsr69.GenerationNameUtil.componentTemplateResourceName;
import static com.axellience.vuegwt.jsr69.GenerationUtil.getUnusableByJSAnnotation;
import static com.axellience.vuegwt.jsr69.GenerationUtil.hasAnnotation;
import static com.axellience.vuegwt.jsr69.component.ComponentGenerationUtil.getSuperComponentType;
import static com.axellience.vuegwt.jsr69.component.ComponentGenerationUtil.getTemplateVisibleMethods;

/**
 * @author Adrien Baron
 */
public class TemplateResourceGenerator
{
    private final Filer filer;

    public TemplateResourceGenerator(ProcessingEnvironment processingEnvironment)
    {
        filer = processingEnvironment.getFiler();
    }

    public void generate(TypeElement component)
    {
        // Template resource abstract class
        ClassName templateResourceName = componentTemplateResourceName(component);

        Builder templateResourceBuilder =
            getTemplateResourceBuilder(component, templateResourceName);

        processFields(component, templateResourceBuilder);
        processComputedMethods(component, templateResourceBuilder, new HashSet<>());
        processTemplateVisibleMethods(component, templateResourceBuilder);

        // And generate our Java Class
        GenerationUtil.toJavaFile(filer, templateResourceBuilder, templateResourceName);
    }

    /**
     * Create the Builder for our {@link TemplateResource} class
     * @param component The {@link VueComponent} to generate for
     * @param templateResourceName The name of our Template resource
     * @return A builder to build the class
     */
    private Builder getTemplateResourceBuilder(TypeElement component,
        ClassName templateResourceName)
    {
        Builder templateResourceBuilder = TypeSpec
            .classBuilder(templateResourceName)
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .addAnnotation(getUnusableByJSAnnotation());

        templateResourceBuilder.addSuperinterface(ParameterizedTypeName.get(ClassName.get(
            TemplateResource.class), ClassName.get(component)));

        return templateResourceBuilder;
    }

    /**
     * Process all the fields in our Component that are visible to the template.
     * We add them to our class, so that expression from the template that will end up in our
     * resource can find them.
     * @param component The {@link VueComponent} to generate for, that contains our fields
     * @param templateResourceBuilder The builder for our {@link TemplateResource}
     */
    private void processFields(TypeElement component, Builder templateResourceBuilder)
    {
        ElementFilter
            .fieldsIn(component.getEnclosedElements())
            .stream()
            .filter(ComponentGenerationUtil::isFieldVisibleInJS)
            .forEach(field -> this.addField(field.asType(),
                field.getSimpleName().toString(),
                templateResourceBuilder));

        getSuperComponentType(component).ifPresent(superType -> processFields(superType,
            templateResourceBuilder));
    }

    private void addField(TypeMirror type, String name, Builder templateResourceBuilder)
    {
        templateResourceBuilder.addField(FieldSpec
            .builder(TypeName.get(type), name, Modifier.PROTECTED)
            .addAnnotation(JsProperty.class)
            .build());
    }

    /**
     * Add attributes for the computed properties.
     * This is needed for the Template expressions to compile in Java.
     * @param component The component we are currently processing
     * @param templateResourceBuilder Builder for the {@link TemplateResource} class
     * @param alreadyDone Name of the computed properties already done
     */
    private void processComputedMethods(TypeElement component, Builder templateResourceBuilder,
        Set<String> alreadyDone)
    {
        ElementFilter
            .methodsIn(component.getEnclosedElements())
            .stream()
            .filter(method -> hasAnnotation(method, Computed.class))
            .forEach(method -> this.processComputedMethod(method,
                templateResourceBuilder,
                alreadyDone));

        getSuperComponentType(component).ifPresent(superType -> processComputedMethods(superType,
            templateResourceBuilder,
            alreadyDone));
    }

    private void processComputedMethod(ExecutableElement method, Builder templateResourceBuilder,
        Set<String> alreadyDone)
    {
        String propertyName = GenerationUtil.getComputedPropertyName(method);
        if (alreadyDone.contains(propertyName))
            return;

        TypeMirror propertyType;
        if ("void".equals(method.getReturnType().toString()))
            propertyType = method.getParameters().get(0).asType();
        else
            propertyType = method.getReturnType();

        this.addField(propertyType, propertyName, templateResourceBuilder);
        alreadyDone.add(propertyName);
    }

    /**
     * Add a stub method to the {@link TemplateResource} for all the methods in the Component
     * @param component The component we are currently processing
     * @param templateResourceBuilder Builder for the {@link TemplateResource} class
     */
    private void processTemplateVisibleMethods(TypeElement component,
        Builder templateResourceBuilder)
    {
        getTemplateVisibleMethods(component)
            .stream()
            .map(this::createJsTypeMethod)
            .forEach(templateResourceBuilder::addMethod);

        getSuperComponentType(component).ifPresent(superType -> processTemplateVisibleMethods(
            superType,
            templateResourceBuilder));
    }

    /**
     * Generate a stub method for {@link VueComponent} method that are visible in the template.
     * This allows template expressions that will end up in our Template resource to find them.
     * This proxy will keep the same name in JS. When it will be called, it will actually
     * call the method from our component.
     * @param originalMethod Original method we want to generate a stub for
     */
    private MethodSpec createJsTypeMethod(ExecutableElement originalMethod)
    {
        MethodSpec.Builder proxyMethodBuilder = MethodSpec
            .methodBuilder(originalMethod.getSimpleName().toString())
            .addModifiers(originalMethod
                .getModifiers()
                .stream()
                .filter(modifier -> modifier != Modifier.NATIVE)
                .collect(Collectors.toList()))
            .addAnnotation(JsMethod.class)
            .returns(ClassName.get(originalMethod.getReturnType()));

        originalMethod
            .getParameters()
            .forEach(parameter -> proxyMethodBuilder.addParameter(TypeName.get(parameter.asType()),
                parameter.getSimpleName().toString()));

        TypeName returnTypeName = TypeName.get(originalMethod.getReturnType());
        if (returnTypeName.equals(TypeName.INT)
            || returnTypeName.equals(TypeName.BYTE)
            || returnTypeName.equals(TypeName.SHORT)
            || returnTypeName.equals(TypeName.LONG)
            || returnTypeName.equals(TypeName.FLOAT)
            || returnTypeName.equals(TypeName.DOUBLE))
        {
            proxyMethodBuilder.addStatement("return 0");
        }
        else if (returnTypeName.equals(TypeName.BOOLEAN))
        {
            proxyMethodBuilder.addStatement("return false");
        }
        else if (!returnTypeName.equals(TypeName.VOID))
        {
            proxyMethodBuilder.addStatement("return null");
        }

        return proxyMethodBuilder.build();
    }
}
