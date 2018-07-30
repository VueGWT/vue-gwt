package com.axellience.vuegwt.processors.component.template.parser.refs;

import javax.lang.model.type.TypeMirror;

public class RefInfo {

  private String name;
  private final TypeMirror elementType;
  private boolean isArray;

  public RefInfo(String name, TypeMirror elementType, boolean isArray) {
    this.name = name;
    this.elementType = elementType;
    this.isArray = isArray;
  }

  public String getName() {
    return name;
  }

  public TypeMirror getElementType() {
    return elementType;
  }

  public boolean isArray() {
    return isArray;
  }
}
