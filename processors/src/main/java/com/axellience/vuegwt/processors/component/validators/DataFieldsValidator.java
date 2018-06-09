package com.axellience.vuegwt.processors.component.validators;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;
import jsinterop.annotations.JsProperty;

public class DataFieldsValidator {

  private final Types types;
  private final TypeMirror setType;
  private final TypeMirror listType;
  private final TypeMirror mapType;
  private final Messager messager;

  public DataFieldsValidator(Types types, Elements elements, Messager messager) {
    this.types = types;
    setType = types.erasure(elements.getTypeElement(Set.class.getCanonicalName()).asType());
    listType = types.erasure(elements.getTypeElement(List.class.getCanonicalName()).asType());
    mapType = types.erasure(elements.getTypeElement(Map.class.getCanonicalName()).asType());
    this.messager = messager;
  }

  public void validateComponentDataField(VariableElement dataField) {
    validateField(dataField, new HashSet<>(), dataField.getEnclosingElement(),
        dataField.getSimpleName().toString());
  }

  private void validateField(VariableElement field, Set<String> exploredTypes, Element component,
      String debugPath) {
    TypeMirror fieldType = field.asType();

    if (hasSuppressWarning(field)) {
      return;
    }

    if (isCollectionField(field)) {
      validateCollectionField(field, component, debugPath);
    }

    validateType(fieldType, exploredTypes, component, debugPath);
  }

  private void validateType(TypeMirror type, Set<String> exploredTypes, Element component,
      String debugPath) {
    if (type.getKind() != TypeKind.DECLARED) {
      return;
    }

    DeclaredType declaredType = (DeclaredType) type;

    String fieldTypeQualifiedName = type.toString();
    if (exploredTypes.contains(fieldTypeQualifiedName)) {
      return;
    }
    exploredTypes.add(fieldTypeQualifiedName);

    if (isJavaBaseType(type)) {
      return;
    }

    declaredType.getTypeArguments()
        .forEach(typeArgument -> validateType(typeArgument, exploredTypes, component,
            debugPath + " -> " + typeArgument));

    Element fieldTypeElement = declaredType.asElement();
    if (fieldTypeElement == null) {
      return;
    }

    List<VariableElement> childrenFields = ElementFilter
        .fieldsIn(fieldTypeElement.getEnclosedElements());
    for (VariableElement childField : childrenFields) {
      validateField(childField, exploredTypes, component,
          debugPath + " -> " + childField.getSimpleName());
    }
  }

  private void validateCollectionField(VariableElement field, Element component, String debugPath) {
    if (!hasJsPropertyAnnotation(field)) {
      messager.printMessage(Kind.MANDATORY_WARNING,
          "Collection with missing @JsProperty annotation: " + debugPath
              + ", please check the Vue GWT doc for Java Collection observation, in Component "
              + component, field);
    }
    if (!isSupportedCollectionField(field)) {
      messager.printMessage(Kind.MANDATORY_WARNING,
          "Specific Collection type used as Property type, you should use either Map, Set or List: "
              + debugPath
              + ", please check the Vue GWT doc for Java Collection observation, in Component"
              + component, field);
    }
  }

  private boolean hasSuppressWarning(VariableElement field) {
    SuppressWarnings annotation = field.getAnnotation(SuppressWarnings.class);
    if (annotation == null) {
      return false;
    }

    for (String s : annotation.value()) {
      if ("vue-gwt-collections".equals(s)) {
        return true;
      }
    }

    return false;
  }

  private boolean isJavaBaseType(TypeMirror fieldType) {
    return fieldType.toString().startsWith("java.lang");
  }

  private boolean hasJsPropertyAnnotation(VariableElement field) {
    return field.getAnnotation(JsProperty.class) != null;
  }

  private boolean isCollectionField(VariableElement field) {
    TypeMirror type = types.erasure(field.asType());

    return types.isAssignable(type, listType) ||
        types.isAssignable(type, setType) ||
        types.isAssignable(type, mapType);
  }

  private boolean isSupportedCollectionField(VariableElement field) {
    TypeMirror type = types.erasure(field.asType());

    return types.isSameType(type, listType) ||
        types.isSameType(type, setType) ||
        types.isSameType(type, mapType);
  }
}
