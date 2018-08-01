package com.axellience.vuegwt.processors.component;

import javax.lang.model.type.TypeMirror;

public class ExposedField {
  private String name;
  private TypeMirror type;

  public ExposedField(String name, TypeMirror type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public TypeMirror getType() {
    return type;
  }
}
