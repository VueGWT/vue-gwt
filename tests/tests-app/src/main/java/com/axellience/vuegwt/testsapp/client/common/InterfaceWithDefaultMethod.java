package com.axellience.vuegwt.testsapp.client.common;

import jsinterop.annotations.JsMethod;

public interface InterfaceWithDefaultMethod {
  @JsMethod
  default String getDefaultText() {
    return "a default method value";
  }
}
