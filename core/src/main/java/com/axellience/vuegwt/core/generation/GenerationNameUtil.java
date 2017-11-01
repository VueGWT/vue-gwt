package com.axellience.vuegwt.core.generation;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;

import javax.inject.Provider;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author Adrien Baron
 */
public class GenerationNameUtil
{
    public static String COMPONENT_JS_TYPE_SUFFIX = "JsType";
    private static String COMPONENT_INJECTED_DEPENDENCIES_SUFFIX = "InjectedDependencies";

    private static String COMPONENT_TEMPLATE_BUNDLE_SUFFIX = "TemplateBundle";
    public static String COMPONENT_TEMPLATE_BUNDLE_METHOD_NAME = "template";

    private static String COMPONENT_FACTORY_SUFFIX = "Factory";

    private static String STYLE_BUNDLE_SUFFIX = "Bundle";
    public static String STYLE_BUNDLE_METHOD_NAME = "style";

    private static String DIRECTIVE_OPTIONS_SUFFIX = "Options";

    public static ClassName componentJsTypeName(TypeElement component)
    {
        return nameWithSuffix(component, COMPONENT_JS_TYPE_SUFFIX);
    }

    public static ClassName componentInjectedDependenciesName(TypeElement component)
    {
        return nameWithSuffix(component, COMPONENT_INJECTED_DEPENDENCIES_SUFFIX);
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

    public static ClassName componentTemplateBundleName(TypeElement component)
    {
        return nameWithSuffix(component, COMPONENT_TEMPLATE_BUNDLE_SUFFIX);
    }

    public static ClassName styleBundleName(TypeElement style)
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
}
