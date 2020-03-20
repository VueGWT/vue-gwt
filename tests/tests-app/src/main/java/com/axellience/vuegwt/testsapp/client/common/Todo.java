package com.axellience.vuegwt.testsapp.client.common;

import jsinterop.annotations.JsProperty;

public class Todo {

  @JsProperty
  private String text;

  public Todo(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }
}
