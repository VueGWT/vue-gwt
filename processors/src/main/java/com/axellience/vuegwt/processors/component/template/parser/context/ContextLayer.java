package com.axellience.vuegwt.processors.component.template.parser.context;

import com.axellience.vuegwt.processors.component.template.parser.variable.DestructuredPropertyInfo;
import com.axellience.vuegwt.processors.component.template.parser.variable.LocalVariableInfo;
import com.axellience.vuegwt.processors.component.template.parser.variable.VariableInfo;
import com.squareup.javapoet.TypeName;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A context layer holding variables visible in a given element. Each v-for will create a new child
 * context. Exiting a v-for will pop the latest context.
 */
class ContextLayer {

  private static final String UNIQUE_LOCAL_VARIABLE_PREFIX = "vg$u";

  private final Map<String, VariableInfo> variables = new HashMap<>();
  private final Set<String> methods = new HashSet<>();
  private int uniqueContextVariableCount;

  ContextLayer(int uniqueContextVariableCount) {
    this.uniqueContextVariableCount = uniqueContextVariableCount;
  }

  private <T extends VariableInfo> T addVariable(T variableInfo) {
    variables.put(variableInfo.getName(), variableInfo);
    return variableInfo;
  }

  VariableInfo addVariable(TypeName type, String name) {
    return addVariable(new VariableInfo(type, name));
  }

  LocalVariableInfo addLocalVariable(String type, String templateName) {
    return addVariable(new LocalVariableInfo(type, templateName));
  }

  LocalVariableInfo addUniqueLocalVariable(String type) {
    return addVariable(
        new LocalVariableInfo(type, UNIQUE_LOCAL_VARIABLE_PREFIX + uniqueContextVariableCount++)
    );
  }

  DestructuredPropertyInfo addDestructuredProperty(String propertyType,
      String propertyName,
      LocalVariableInfo destructuredVariable) {
    return addVariable(
        new DestructuredPropertyInfo(propertyType, propertyName, destructuredVariable));
  }

  VariableInfo getVariableInfo(String name) {
    return variables.get(name);
  }

  void addMethod(String methodName) {
    this.methods.add(methodName);
  }

  boolean hasMethod(String methodName) {
    return this.methods.contains(methodName);
  }

  int getUniqueContextVariableCount() {
    return uniqueContextVariableCount;
  }
}