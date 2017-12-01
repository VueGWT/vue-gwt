package com.axellience.vuegwt.core.generation;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Emit;
import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.client.directive.VueDirective;
import com.google.gwt.regexp.shared.RegExp;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;

import javax.inject.Provider;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author Adrien Baron
 */
public class GenerationNameUtil
{
    public static String COMPONENT_JS_TYPE_SUFFIX = "JsType";
    private static String COMPONENT_INJECTED_DEPENDENCIES_SUFFIX = "InjectedDependencies";

    public static String COMPONENT_TEMPLATE_SUFFIX = "Template";

    private static String COMPONENT_FACTORY_SUFFIX = "Factory";

    private static String STYLE_BUNDLE_SUFFIX = "Bundle";
    public static String STYLE_BUNDLE_METHOD_NAME = "style";

    private static String DIRECTIVE_OPTIONS_SUFFIX = "Options";

    private static RegExp CAMEL_CASE_PATTERN = RegExp.compile("([a-z])([A-Z]+)", "g");
    private static RegExp COMPONENT_SUFFIX_REGEX = RegExp.compile("Component$");
    private static RegExp DIRECTIVE_SUFFIX_REGEX = RegExp.compile("Directive$");

    public static ClassName componentJsTypeName(TypeElement component)
    {
        return nameWithSuffix(component, COMPONENT_JS_TYPE_SUFFIX);
    }

    public static ClassName componentJsTypeName(ClassName component)
    {
        return nameWithSuffix(component, COMPONENT_JS_TYPE_SUFFIX);
    }

    public static ClassName componentInjectedDependenciesName(TypeElement component)
    {
        return nameWithSuffix(component, COMPONENT_INJECTED_DEPENDENCIES_SUFFIX);
    }

    public static ClassName componentTemplateName(TypeElement component)
    {
        return nameWithSuffix(component, COMPONENT_TEMPLATE_SUFFIX);
    }

    public static ClassName componentTemplateName(ClassName component)
    {
        return nameWithSuffix(component, COMPONENT_TEMPLATE_SUFFIX);
    }

    public static ClassName componentTemplateImplName(ClassName component)
    {
        return nameWithSuffix(component, COMPONENT_TEMPLATE_SUFFIX + "Impl");
    }

    public static ClassName componentFactoryName(TypeElement component)
    {
        return nameWithSuffix(component, COMPONENT_FACTORY_SUFFIX);
    }

    public static ClassName componentFactoryName(TypeMirror component)
    {
        return nameWithSuffix(component, COMPONENT_FACTORY_SUFFIX);
    }

    public static ClassName componentFactoryName(Class<?> component)
    {
        return nameWithSuffix(component, COMPONENT_FACTORY_SUFFIX);
    }

    public static ClassName componentFactoryName(ClassName component)
    {
        return nameWithSuffix(component, COMPONENT_FACTORY_SUFFIX);
    }

    public static ClassName styleBundleName(TypeElement style)
    {
        return nameWithSuffix(style, STYLE_BUNDLE_SUFFIX);
    }

    public static ClassName styleBundleName(ClassName style)
    {
        return nameWithSuffix(style, STYLE_BUNDLE_SUFFIX);
    }

    public static ClassName styleBundleName(String styleQualifiedName)
    {
        return nameWithSuffix(styleQualifiedName, STYLE_BUNDLE_SUFFIX);
    }

    public static ClassName directiveOptionsName(TypeElement directive)
    {
        return nameWithSuffix(directive, DIRECTIVE_OPTIONS_SUFFIX);
    }

    public static ClassName directiveOptionsName(TypeMirror directive)
    {
        return nameWithSuffix(directive, DIRECTIVE_OPTIONS_SUFFIX);
    }

    public static ClassName directiveOptionsName(Class<?> directive)
    {
        return nameWithSuffix(directive, DIRECTIVE_OPTIONS_SUFFIX);
    }

    public static ClassName nameWithSuffix(TypeElement type, String suffix)
    {
        return nameWithSuffix(type.getQualifiedName().toString(), suffix);
    }

    public static ClassName nameWithSuffix(TypeMirror type, String suffix)
    {
        return nameWithSuffix(type.toString(), suffix);
    }

    public static ClassName nameWithSuffix(Class<?> type, String suffix)
    {
        return nameWithSuffix(type.getCanonicalName(), suffix);
    }

    public static ClassName nameWithSuffix(ClassName className, String suffix)
    {
        return nameWithSuffix(className.reflectionName(), suffix);
    }

    public static ClassName nameWithSuffix(String typeQualifiedName, String suffix)
    {
        return ClassName.bestGuess(typeQualifiedName + suffix);
    }

    public static ParameterizedTypeName providerOf(ClassName className)
    {
        return ParameterizedTypeName.get(ClassName.get(Provider.class), className);
    }

    public static String getPackage(TypeElement typeElement)
    {
        return ClassName.get(typeElement).packageName();
    }

    /**
     * Return the default name to register a component based on it's class name.
     * The name of the tag is the name of the component converted to kebab-case.
     * If the component class ends with "Component", this part is ignored.
     * @param componentClass The Element representing the class of the {@link VueComponent} we want
     * the name of
     * @return The name of the component as kebab case
     */
    public static String componentToTagName(Element componentClass)
    {
        Component componentAnnotation = componentClass.getAnnotation(Component.class);
        if (!"".equals(componentAnnotation.name()))
            return componentAnnotation.name();

        // Drop "Component" at the end of the class name
        String componentClassName = componentClass.getSimpleName().toString();
        componentClassName = COMPONENT_SUFFIX_REGEX.replace(componentClassName, "");
        // Convert from CamelCase to kebab-case
        return CAMEL_CASE_PATTERN.replace(componentClassName, "$1-$2").toLowerCase();
    }

    /**
     * Return the default name to register a directive based on it's class name
     * The name of the tag is the name of the component converted to kebab-case
     * If the component class ends with "Directive", this part is ignored
     * @param directiveClassName The class name of the {@link VueDirective}
     * @return The name of the directive as kebab case
     */
    public static String directiveToTagName(String directiveClassName)
    {
        // Drop "Component" at the end of the class name
        directiveClassName = DIRECTIVE_SUFFIX_REGEX.replace(directiveClassName, "");
        // Convert from CamelCase to kebab-case
        return CAMEL_CASE_PATTERN.replace(directiveClassName, "$1-$2").toLowerCase();
    }

    /**
     * Return the name of the event to emit for a given method.
     * Expect the method to be annotated with {@link com.axellience.vuegwt.core.annotations.component.Emit}.
     * @param method The method to convert
     * @return The name of the event
     */
    public static String methodToEventName(ExecutableElement method)
    {
        Emit emitAnnotation = method.getAnnotation(Emit.class);
        if (!"".equals(emitAnnotation.value()))
            return emitAnnotation.value();

        return CAMEL_CASE_PATTERN.replace(method.getSimpleName().toString(), "$1-$2").toLowerCase();
    }
}
