package com.axellience.vuegwt.processors.component.template.parser;

import com.axellience.vuegwt.processors.component.template.parser.context.TemplateParserContext;
import com.axellience.vuegwt.processors.component.template.parser.variable.LocalVariableInfo;
import jsinterop.base.JsPropertyMap;

class SlotScopeDefinition {

  private final String value;
  private final TemplateParserContext context;
  private final TemplateParserLogger logger;
  private String slotScopeVariableName;

  public SlotScopeDefinition(String value, TemplateParserContext context,
      TemplateParserLogger logger) {

    this.value = value.trim();
    this.context = context;
    this.logger = logger;

    if (value.startsWith("{")) {
      processParameterDestructuring();
    } else {
      processSimpleSlotScope();
    }
  }

  private void processSimpleSlotScope() {
    slotScopeVariableName = value;
    context.addLocalVariable(JsPropertyMap.class.getCanonicalName(), value);
  }

  private void processParameterDestructuring() {
    LocalVariableInfo destructuredVariable = context
        .addUniqueLocalVariable(JsPropertyMap.class.getCanonicalName());
    slotScopeVariableName = destructuredVariable.getName();

    String[] variables = value.substring(1, value.length() - 1).split(",");
    for (String property : variables) {
      processDestructuredProperty(property, destructuredVariable);
    }
  }

  private void processDestructuredProperty(String property,
      LocalVariableInfo destructuredVariable) {
    property = property.trim();
    String[] propertySplit = property.split(" ");

    if (propertySplit.length != 2) {
      logger.error(
          "Invalid slot scope definition found. Must be in the form {String myStringValue, int myIntValue}",
          value);
      return;
    }

    String propertyType = context.getFullyQualifiedNameForClassName(propertySplit[0].trim());
    String propertyName = propertySplit[1].trim();
    context.addDestructuredProperty(propertyType, propertyName, destructuredVariable);
  }

  String getSlotScopeVariableName() {
    return slotScopeVariableName;
  }
}
