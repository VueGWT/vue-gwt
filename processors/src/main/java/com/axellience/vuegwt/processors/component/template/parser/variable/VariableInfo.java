package com.axellience.vuegwt.processors.component.template.parser.variable;

import static com.axellience.vuegwt.processors.utils.GeneratorsUtil.stringTypeToTypeName;

import com.squareup.javapoet.TypeName;

/**
 * Information about a Variable in the template. Can be for example a variable accessible from Java
 * (@JsProperty), a Computed Property a loop variable (v-for) or method parameters.
 */
public class VariableInfo {

  private TypeName type;
  private String name;

  public VariableInfo(String type, String name) {
    this(stringTypeToTypeName(type), name);
  }

  public VariableInfo(TypeName type, String name) {
    this.type = type;
    this.name = name;
  }

  public TypeName getType() {
    return type;
  }

  public String getName() {
    return name;
  }
}
