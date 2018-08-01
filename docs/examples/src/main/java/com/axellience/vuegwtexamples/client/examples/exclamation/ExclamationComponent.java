package com.axellience.vuegwtexamples.client.examples.exclamation;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsMethod;

@Component
public class ExclamationComponent implements IsVueComponent {

  @Data
  String message = "Hello Vue GWT!";

  @JsMethod // This is added so the example can be interacted with in documentation
  public void addExclamationMark() {
    this.message += "!";
  }
}