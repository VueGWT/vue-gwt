package com.axellience.vuegwt.processors.utils;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwt.core.annotations.component.PropDefault;
import com.axellience.vuegwt.core.annotations.component.PropValidator;
import com.axellience.vuegwt.core.annotations.component.Watch;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasRender;
import com.axellience.vuegwt.core.client.component.options.CustomizeOptions;
import com.axellience.vuegwt.core.client.vue.VueComponentFactory;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.axellience.vuegwt.processors.utils.GeneratorsNameUtil.componentFactoryName;
import static com.axellience.vuegwt.processors.utils.GeneratorsUtil.hasAnnotation;
import static com.axellience.vuegwt.processors.utils.GeneratorsUtil.hasInterface;

/**
 * Utilities methods to manipulate the {@link Component} annotation
 * @author Adrien Baron
 */
public class ComponentGeneratorsUtil
{
    /**
     * Return the list of {@link IsVueComponent} dependencies listed on {@link
     * Component#components()}.
     * When retrieving this list, it can produce a {@link MirroredTypesException}.
     * The case is managed here and we always returns {@link TypeMirror} of the dependencies.
     * @param elementsUtil The Element utils provided by the annotation processor environement.
     * @param component The {@link Component} annotation to process
     * @return The list of TypeMirror of the {@link IsVueComponent} the {@link Component} depends on
     */
    public static List<TypeMirror> getComponentLocalComponents(Elements elementsUtil,
        TypeElement component)
    {
        Component componentAnnotation = component.getAnnotation(Component.class);

        try
        {
            Class<?>[] componentsClass = componentAnnotation.components();

            return Stream
                .of(componentsClass)
                .map(Class::getCanonicalName)
                .map(elementsUtil::getTypeElement)
                .map(TypeElement::asType)
                .collect(Collectors.toList());
        }
        catch (MirroredTypesException mte)
        {
            return new LinkedList<>(mte.getTypeMirrors());
        }
    }

    /**
     * Return the list of {@link CustomizeOptions} that will be used to customize the options
     * before passing them down to Vue.js.
     * When retrieving this list, it can produce a {@link MirroredTypesException}.
     * The case is managed here and we always returns {@link TypeMirror} of the items.
     * @param elementsUtil The Element utils provided by the annotation processor environement.
     * @param component The {@link Component} annotation to process
     * @return The list of TypeMirror of the {@link CustomizeOptions} the {@link Component} depends on
     */
    public static List<TypeMirror> getComponentCustomizeOptions(Elements elementsUtil,
        TypeElement component)
    {
        Component componentAnnotation = component.getAnnotation(Component.class);

        try
        {
            Class<?>[] componentsClass = componentAnnotation.customizeOptions();

            return Stream
                .of(componentsClass)
                .map(Class::getCanonicalName)
                .map(elementsUtil::getTypeElement)
                .map(TypeElement::asType)
                .collect(Collectors.toList());
        }
        catch (MirroredTypesException mte)
        {
            return new LinkedList<>(mte.getTypeMirrors());
        }
    }

    /**
     * Resolve a variable element {@link TypeName}. If the type cannot be resolved (the TypeMirror
     * is of
     * kind ERROR), displays an explicit message.
     * @param variableElement The variable we want to resolve the type of
     * @param messager A messager to display the error if any
     * @return The {@link TypeName} of our variable type.
     */
    public static TypeName resolveVariableTypeName(VariableElement variableElement,
        Messager messager)
    {
        // Resolve type
        if (variableElement.asType().getKind() == TypeKind.ERROR)
        {
            messager.printMessage(Kind.ERROR,
                "Couldn't resolve type "
                    + variableElement.asType()
                    + " for variable "
                    + variableElement
                    + ". If you are trying to inject a ComponentFactory inside a Component, please inject VueComponentFactory<MyComponent> instead.");
        }

        TypeName typeName = ClassName.get(variableElement.asType());
        if (typeName instanceof ParameterizedTypeName)
        {
            ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) typeName;
            if (parameterizedTypeName.rawType.equals(ClassName.get(VueComponentFactory.class)))
            {
                return componentFactoryName((ClassName) parameterizedTypeName.typeArguments.get(0));
            }
        }

        return typeName;
    }

    /**
     * Check if a given method is usable in the template
     * @param method The method we are checking
     * @return true if visible, false otherwise
     */
    public static boolean isMethodVisibleInTemplate(ExecutableElement method)
    {
        return isMethodVisibleInJS(method)
            && !hasAnnotation(method, Computed.class)
            && !hasAnnotation(method, Watch.class)
            && !hasAnnotation(method, PropValidator.class)
            && !hasAnnotation(method, PropDefault.class)
            && !method.getModifiers().contains(Modifier.STATIC)
            && !method.getModifiers().contains(Modifier.PRIVATE);
    }

    /**
     * Return weather a given field is visible in JS (JsInterop).
     * It will be the case if it's public and it's class has the {@link JsType} annotation, or
     * if it has the {@link JsProperty} annotation.
     * @param field The field to check
     * @return true if it is visible (JsInterop), false otherwise
     */
    public static boolean isFieldVisibleInJS(VariableElement field)
    {
        return (hasAnnotation(field.getEnclosingElement(), JsType.class) && field
            .getModifiers()
            .contains(Modifier.PUBLIC)) || hasAnnotation(field, JsProperty.class);
    }

    /**
     * Return weather a given method is visible in JS (JsInterop).
     * It will be the case if it's public and it's class/interface has the {@link JsType}
     * annotation, or
     * if it has the {@link JsMethod} annotation.
     * @param method The method to check
     * @return true if it is visible (JsInterop), false otherwise
     */
    public static boolean isMethodVisibleInJS(ExecutableElement method)
    {
        return (hasAnnotation(method.getEnclosingElement(), JsType.class) && method
            .getModifiers()
            .contains(Modifier.PUBLIC)) || hasAnnotation(method, JsMethod.class);
    }

    /**
     * Return the {@link TypeElement} of the parent {@link IsVueComponent} of a given {@link
     * IsVueComponent}.
     * If the super class of the {@link IsVueComponent} is just {@link IsVueComponent}, return an empty
     * {@link Optional}
     * @param component The {@link IsVueComponent} to get the super type of
     * @return The {@link TypeElement} of the super {@link IsVueComponent} or an empty {@link Optional}
     */
    public static Optional<TypeElement> getSuperComponentType(TypeElement component)
    {
        // If super type is vue component, don't return it
        if (TypeName.get(Object.class).equals(TypeName.get(component.getSuperclass())))
            return Optional.empty();

        return Optional.of((TypeElement) ((DeclaredType) component.getSuperclass()).asElement());
    }

    /**
     * Return the number of super component in the chain of parents
     * @param component The {@link IsVueComponent} to count the super type of
     * @return The number of super components
     */
    public static int getSuperComponentCount(TypeElement component)
    {
        return getSuperComponentType(component)
            .map(superComponent -> getSuperComponentCount(superComponent) + 1)
            .orElse(0);
    }

    /**
     * Check if the given Component has a Template.
     * It doesn't have a template if the class is abstract, if it implements render function
     * or if it has the flag "hasTemplate" to false on the component annotation.
     * @param processingEnvironment Environment of the Annotation processor
     * @param component The component to check
     * @return true if has a template, false otherwise
     */
    public static boolean hasTemplate(ProcessingEnvironment processingEnvironment,
        TypeElement component)
    {
        Component annotation = component.getAnnotation(Component.class);
        if (annotation == null || !annotation.hasTemplate())
            return false;

        if (component.getModifiers().contains(Modifier.ABSTRACT))
            return false;

        return !hasInterface(processingEnvironment, component.asType(), HasRender.class);
    }
}