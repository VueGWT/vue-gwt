package com.axellience.vuegwt.core.client.component.options.props;

public class PropProxyDefinition {
  private String propName;
  private String fieldName;

  public PropProxyDefinition(String propName, String fieldName) {
    this.propName = propName;
    this.fieldName = fieldName;
  }

  public String getPropName() {
    return propName;
  }

  public String getFieldName() {
    return fieldName;
  }
}
