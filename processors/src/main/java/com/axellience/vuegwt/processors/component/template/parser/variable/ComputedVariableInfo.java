package com.axellience.vuegwt.processors.component.template.parser.variable;

import com.squareup.javapoet.TypeName;

public class ComputedVariableInfo extends VariableInfo {

  private final String fieldName;

  public ComputedVariableInfo(TypeName type, String computedPropertyName, String fieldName) {
    super(type, computedPropertyName);
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }
}
