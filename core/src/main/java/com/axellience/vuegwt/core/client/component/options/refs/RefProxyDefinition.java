package com.axellience.vuegwt.core.client.component.options.refs;

public class RefProxyDefinition {
  private String refName;
  private String fieldName;

  public RefProxyDefinition(String refName, String fieldName) {
    this.refName = refName;
    this.fieldName = fieldName;
  }

  public String getRefName() {
    return refName;
  }

  public String getFieldName() {
    return fieldName;
  }
}
