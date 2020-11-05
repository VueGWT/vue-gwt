package com.axellience.vuegwt.processors.component.template.parser.variable;

/**
 * Hold info about a destructured property. For example in slot scope: slot-scope="{int myProp}",
 * this class will hold the info for myProp.
 */
public class DestructuredPropertyInfo extends VariableInfo {

  private final LocalVariableInfo destructuredVariable;

  public DestructuredPropertyInfo(String propertyType, String propertyName,
      LocalVariableInfo destructuredVariable) {
    super(propertyType, propertyName);
    this.destructuredVariable = destructuredVariable;
  }

  public LocalVariableInfo getDestructuredVariable() {
    return destructuredVariable;
  }

  public String getAsDestructuredValue() {
    if (getType().isPrimitive()) {
      String typeName = getType().toString();
      String anyGetterName =
          "as" + typeName.substring(0, 1).toUpperCase() + typeName.substring(1) + "()";
      return destructuredVariable.getName() + ".getAsAny(\"" + getName() + "\")." + anyGetterName;
    }

    return "((" + getType() + ") " + destructuredVariable.getName() + ".get(\""
        + getName() + "\"))";
  }
}
