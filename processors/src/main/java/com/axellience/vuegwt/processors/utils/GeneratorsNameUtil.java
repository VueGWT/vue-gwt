package com.axellience.vuegwt.processors.utils;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Emit;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.directive.VueDirective;
import com.google.common.base.CaseFormat;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import javax.inject.Provider;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Name
 *
 * @author Adrien Baron
 */
public class GeneratorsNameUtil {

  private static String COMPONENT_EXPOSED_TYPE_SUFFIX = "ExposedType";
  private static String COMPONENT_INJECTED_DEPENDENCIES_SUFFIX = "InjectedDependencies";

  private static String COMPONENT_FACTORY_SUFFIX = "Factory";

  private static String DIRECTIVE_OPTIONS_SUFFIX = "Options";

  public static ClassName componentExposedTypeName(TypeElement component) {
    return nameWithSuffix(component, COMPONENT_EXPOSED_TYPE_SUFFIX);
  }

  public static ClassName componentExposedTypeName(ClassName component) {
    return nameWithSuffix(component, COMPONENT_EXPOSED_TYPE_SUFFIX);
  }

  public static ClassName componentInjectedDependenciesName(TypeElement component) {
    return nameWithSuffix(component, COMPONENT_INJECTED_DEPENDENCIES_SUFFIX);
  }

  public static ClassName componentFactoryName(TypeElement component) {
    return nameWithSuffix(component, COMPONENT_FACTORY_SUFFIX);
  }

  public static ClassName componentFactoryName(TypeMirror component) {
    return nameWithSuffix(component, COMPONENT_FACTORY_SUFFIX);
  }

  public static ClassName componentFactoryName(Class<?> component) {
    return nameWithSuffix(component, COMPONENT_FACTORY_SUFFIX);
  }

  public static ClassName componentFactoryName(ClassName component) {
    return nameWithSuffix(component, COMPONENT_FACTORY_SUFFIX);
  }

  public static ClassName directiveOptionsName(TypeElement directive) {
    return nameWithSuffix(directive, DIRECTIVE_OPTIONS_SUFFIX);
  }

  public static ClassName directiveOptionsName(TypeMirror directive) {
    return nameWithSuffix(directive, DIRECTIVE_OPTIONS_SUFFIX);
  }

  public static ClassName directiveOptionsName(Class<?> directive) {
    return nameWithSuffix(directive, DIRECTIVE_OPTIONS_SUFFIX);
  }

  public static ClassName nameWithSuffix(TypeElement type, String suffix) {
    return nameWithSuffix(type.getQualifiedName().toString(), suffix);
  }

  public static ClassName nameWithSuffix(TypeMirror type, String suffix) {
    return nameWithSuffix(type.toString(), suffix);
  }

  public static ClassName nameWithSuffix(Class<?> type, String suffix) {
    return nameWithSuffix(type.getCanonicalName(), suffix);
  }

  public static ClassName nameWithSuffix(ClassName className, String suffix) {
    return nameWithSuffix(className.reflectionName(), suffix);
  }

  public static ClassName nameWithSuffix(String typeQualifiedName, String suffix) {
    return ClassName.bestGuess(typeQualifiedName + suffix);
  }

  public static ParameterizedTypeName providerOf(ClassName className) {
    return ParameterizedTypeName.get(ClassName.get(Provider.class), className);
  }

  public static String getPackage(TypeElement typeElement) {
    return ClassName.get(typeElement).packageName();
  }

  public static String vModelFieldToPlaceHolderField(String fieldName) {
    return fieldName + "__vuegwt__vmodel";
  }

  /**
   * Return the default name to register a component based on it's class name. The name of the tag
   * is the name of the component converted to kebab-case. If the component class ends with
   * "Component", this part is ignored.
   *
   * @param componentClassName The Class name of the {@link IsVueComponent} we want the name of
   * @param componentAnnotation The {@link Component} annotation for the {@link IsVueComponent} we
   * want the name of
   * @return The name of the component as kebab case
   */
  public static String componentToTagName(String componentClassName,
      Component componentAnnotation) {
    if (componentAnnotation != null && !"".equals(componentAnnotation.name())) {
      return componentAnnotation.name();
    }

    // Drop "Component" at the end of the class name
    componentClassName = componentClassName.replaceAll("Component$", "");
    // Convert from CamelCase to kebab-case
    return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, componentClassName).toLowerCase();
  }

  /**
   * Return the default name to register a directive based on it's class name The name of the tag is
   * the name of the component converted to kebab-case If the component class ends with "Directive",
   * this part is ignored
   *
   * @param directiveClassName The class name of the {@link VueDirective}
   * @return The name of the directive as kebab case
   */
  public static String directiveToTagName(String directiveClassName) {
    // Drop "Component" at the end of the class name
    directiveClassName = directiveClassName.replaceAll("Directive$", "");
    // Convert from CamelCase to kebab-case
    return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, directiveClassName).toLowerCase();
  }

  /**
   * Return the name of the event to emit for a given method. Expect the method to be annotated with
   * {@link Emit}.
   *
   * @param method The method to convert
   * @return The name of the event
   */
  public static String methodToEventName(ExecutableElement method) {
    Emit emitAnnotation = method.getAnnotation(Emit.class);
    if (!"".equals(emitAnnotation.value())) {
      return emitAnnotation.value();
    }

    return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, method.getSimpleName().toString())
        .toLowerCase();
  }

  /**
   * Return the name of the HTML property to use for a given java {@link Prop} field.
   *
   * @param propName The name of the Java {@link Prop}
   * @return The name of the HTML attribute to use for that prop
   */
  public static String propNameToAttributeName(String propName) {
    return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, propName).toLowerCase();
  }
}
