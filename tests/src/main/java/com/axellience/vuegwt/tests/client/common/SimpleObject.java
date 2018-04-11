package com.axellience.vuegwt.tests.client.common;

import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

public class SimpleObject {

  @JsProperty
  private String stringProperty;

  public SimpleObject() {
    this.stringProperty = null;
  }

  public String getStringProperty() {
    return stringProperty;
  }

  @JsMethod
  public void setStringProperty(String stringProperty) {
    this.stringProperty = stringProperty;
  }
}
