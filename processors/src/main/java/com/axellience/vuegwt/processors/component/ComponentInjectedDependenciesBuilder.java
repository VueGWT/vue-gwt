package com.axellience.vuegwt.processors.component;

import static com.axellience.vuegwt.processors.utils.ComponentGeneratorsUtil.resolveVariableTypeName;
import static com.axellience.vuegwt.processors.utils.GeneratorsNameUtil.componentInjectedDependenciesName;
import static com.axellience.vuegwt.processors.utils.InjectedDependenciesUtil.getInjectedFields;
import static com.axellience.vuegwt.processors.utils.InjectedDependenciesUtil.getInjectedMethods;
import static com.axellience.vuegwt.processors.utils.InjectedDependenciesUtil.hasInjectAnnotation;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.processors.utils.GeneratorsUtil;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Inject;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic.Kind;

/**
 * Build a class used to inject dependencies of a given {@link IsVueComponent}.
 *
 * @author Adrien Baron
 */
public class ComponentInjectedDependenciesBuilder {

  private final Messager messager;
  private final Builder builder;

  private final List<String> injectedFieldsName = new LinkedList<>();
  private final Map<String, List<String>> injectedParametersByMethod = new HashMap<>();

  public ComponentInjectedDependenciesBuilder(ProcessingEnvironment processingEnvironment,
      TypeElement component) {
    this.messager = processingEnvironment.getMessager();

    // Template resource abstract class
    ClassName componentInjectedDependenciesName = componentInjectedDependenciesName(component);

    builder = TypeSpec
        .classBuilder(componentInjectedDependenciesName)
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(AnnotationSpec
            .builder(Generated.class)
            .addMember("value",
                "$S",
                ComponentInjectedDependenciesBuilder.class.getCanonicalName())
            .addMember("date", "$S", new Date().toString())
            .addMember("comments", "$S", "https://github.com/Axellience/vue-gwt")
            .build());

    processInjectedFields(component);
    processInjectedMethods(component);

    if (hasInjectedDependencies()) {
      builder.addMethod(MethodSpec
          .constructorBuilder()
          .addModifiers(Modifier.PUBLIC)
          .addAnnotation(Inject.class)
          .build());

      // Generate the file
      GeneratorsUtil.toJavaFile(processingEnvironment.getFiler(), builder,
          componentInjectedDependenciesName(component),
          component);
    }
  }

  /**
   * Process all the injected fields from our Component.
   *
   * @param component The {@link IsVueComponent} we are processing
   */
  private void processInjectedFields(TypeElement component) {
    getInjectedFields(component).stream().peek(this::validateField).forEach(field -> {
      String fieldName = field.getSimpleName().toString();
      addInjectedVariable(field, fieldName);
      injectedFieldsName.add(fieldName);
    });
  }

  /**
   * Process all the injected methods from our Component.
   *
   * @param component The {@link IsVueComponent} we are processing
   */
  private void processInjectedMethods(TypeElement component) {
    getInjectedMethods(component)
        .stream()
        .peek(this::validateMethod)
        .forEach(this::processInjectedMethod);
  }

  /**
   * Validate properties. This will make sure that @Inject properties are not private or final. We
   * can't inject final or private properties are injection is done at runtime by copying over
   * properties from an injected instance of our Java Component.
   *
   * @param injectedField The property to validate
   */
  private void validateField(VariableElement injectedField) {
    if (injectedField.getModifiers().contains(Modifier.PRIVATE)) {
      messager.printMessage(Kind.ERROR,
          "Property "
              + injectedField.getSimpleName()
              + " in "
              + injectedField
              .getEnclosingElement()
              .getSimpleName()
              + " cannot be injected and private. Please make it at least package protected.");
    }
    if (injectedField.getModifiers().contains(Modifier.FINAL)) {
      messager.printMessage(Kind.ERROR,
          "Property " + injectedField.getSimpleName() + " in " + injectedField
              .getEnclosingElement()
              .getSimpleName() + " cannot be injected and final.");
    }
  }

  private void processInjectedMethod(ExecutableElement injectedMethod) {
    String methodName = injectedMethod.getSimpleName().toString();
    List<String> injectedParameters = new LinkedList<>();
    injectedParametersByMethod.put(methodName, injectedParameters);

    processInjectedMethod(injectedMethod, injectedParameters);
  }

  private void processInjectedMethod(ExecutableElement injectedMethod,
      List<String> injectedParameters) {
    String methodName;
    if (injectedMethod.getKind() == ElementKind.CONSTRUCTOR) {
      methodName = "constructor$";
    } else {
      methodName = injectedMethod.getSimpleName().toString();
    }
    injectedMethod.getParameters().forEach(injectedParameter -> {
      String parameterName = methodName + "_" + injectedParameter.getSimpleName().toString();
      addInjectedVariable(injectedParameter, parameterName);
      injectedParameters.add(parameterName);
    });
  }

  private void validateMethod(ExecutableElement injectedMethod) {
    if (injectedMethod.getModifiers().contains(Modifier.PRIVATE)) {
      messager.printMessage(Kind.ERROR,
          "Method "
              + injectedMethod.getSimpleName()
              + " in "
              + injectedMethod
              .getEnclosingElement()
              .getSimpleName()
              + " cannot be injected and private. Please make it at least package protected.");
    }
  }

  /**
   * Add an injected variable to our component
   *
   * @param element The {@link VariableElement} that was injected
   * @param fieldName The name of the field
   */
  private void addInjectedVariable(VariableElement element, String fieldName) {
    TypeName typeName = resolveVariableTypeName(element, messager);

    // Create field
    FieldSpec.Builder fieldBuilder = FieldSpec.builder(typeName, fieldName, Modifier.PUBLIC);

    // Copy field annotations
    element
        .getAnnotationMirrors()
        .stream()
        .map(AnnotationSpec::get)
        .forEach(fieldBuilder::addAnnotation);

    // If the variable element is a method parameter, it might not have the Inject annotation
    if (!hasInjectAnnotation(element)) {
      fieldBuilder.addAnnotation(Inject.class);
    }

    // And add field
    builder.addField(fieldBuilder.build());
  }

  /**
   * Return true if the component instance has injected dependencies
   *
   * @return true if the component instance has injected dependencies, false otherwise
   */
  public boolean hasInjectedDependencies() {
    return !injectedFieldsName.isEmpty() || !injectedParametersByMethod.isEmpty();
  }

  List<String> getInjectedFieldsName() {
    return injectedFieldsName;
  }

  Map<String, List<String>> getInjectedParametersByMethod() {
    return injectedParametersByMethod;
  }
}
