package com.axellience.vuegwt.processors.component.template.parser.refs;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import elemental2.core.JsArray;
import elemental2.dom.Element;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

public class RefFieldValidator {

  private TypeElement component;
  private Map<String, RefInfo> templateRefsMap;
  private Types types;
  private Elements elements;
  private Messager messager;

  public RefFieldValidator(TypeElement component, Set<RefInfo> templateRefs,
      ProcessingEnvironment processingEnvironment) {
    this.component = component;
    templateRefsMap = new HashMap<>();
    types = processingEnvironment.getTypeUtils();
    messager = processingEnvironment.getMessager();
    elements = processingEnvironment.getElementUtils();

    for (RefInfo templateRef : templateRefs) {
      this.templateRefsMap.put(templateRef.getName(), templateRef);
    }
  }

  public void validateRefField(VariableElement refField) {
    String fieldName = refField.getSimpleName().toString();
    if (!templateRefsMap.containsKey(fieldName)) {
      printError("Couldn't find ref attribute in template for @Ref field \"" + fieldName + "\"");
      return;
    }

    RefInfo refInfo = templateRefsMap.get(fieldName);
    TypeMirror fieldType = refField.asType();
    if (isJsArray(fieldType)) {
      validateRefArrayField(fieldName, refInfo, fieldType);
    } else {
      validateRefField(fieldName, refInfo, fieldType);
    }
  }

  private void validateRefArrayField(String fieldName, RefInfo refElementInfo,
      TypeMirror fieldType) {
    TypeMirror elementType = refElementInfo.getElementType();

    if (!refElementInfo.isArray()) {
      printError(
          "@Ref field \"" + fieldName
              + "\", must not be a JsArray as the ref is not used inside a v-for.");
      return;
    }

    TypeMirror jsArrayTypeParameter = getJsArrayTypeParameter(fieldType);
    if (!isRefValidType(jsArrayTypeParameter)) {
      printError(
          "Invalid type for @Ref array \"" + fieldName
              + "\", must be a JsArray of a type extending elemental2.Element or IsVueComponent.");
      return;
    }

    if (elementType != null && !types.isAssignable(elementType, jsArrayTypeParameter)) {
      printError(
          "Invalid type for @Ref \"" + fieldName
              + "\", must be able to assign \"" + elementType.toString() + "\".");
    }
  }

  private void validateRefField(String fieldName, RefInfo refElementInfo, TypeMirror fieldType) {
    TypeMirror elementType = refElementInfo.getElementType();

    if (refElementInfo.isArray()) {
      printError(
          "@Ref field \"" + fieldName
              + "\", must be a JsArray as the ref is used inside a v-for.");
      return;
    }

    if (!isRefValidType(fieldType)) {
      printError("Invalid type for @Ref \"" + fieldName
          + "\", type must extend elemental2.Element or IsVueComponent.");
      return;
    }

    if (elementType != null && !types.isAssignable(elementType, fieldType)) {
      printError(
          "Invalid type for @Ref \"" + fieldName
              + "\", must be able to assign \"" + elementType.toString() + "\".");
    }
  }

  @Nullable
  private TypeMirror getJsArrayTypeParameter(TypeMirror fieldType) {
    if (!(fieldType instanceof DeclaredType)) {
      return null;
    }

    DeclaredType declaredType = (DeclaredType) fieldType;
    if (declaredType.getTypeArguments().isEmpty()) {
      return null;
    }

    return declaredType.getTypeArguments().get(0);
  }

  private boolean isJsArray(TypeMirror fieldType) {
    return types.isAssignable(types.erasure(fieldType),
        types.erasure(elements.getTypeElement(JsArray.class.getCanonicalName()).asType()));
  }

  private boolean isRefValidType(TypeMirror type) {
    return
        types.isAssignable(type, elements.getTypeElement(Element.class.getCanonicalName()).asType())
            ||
            types.isAssignable(type,
                elements.getTypeElement(IsVueComponent.class.getCanonicalName()).asType());
  }

  private void printError(String message) {
    messager.printMessage(Kind.ERROR,
        message + " In VueComponent: " + component.getQualifiedName(), component);
  }
}
