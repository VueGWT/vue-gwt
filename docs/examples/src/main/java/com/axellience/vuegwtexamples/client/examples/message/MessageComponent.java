package com.axellience.vuegwtexamples.client.examples.message;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsProperty;

@Component
public class MessageComponent implements IsVueComponent {

  @Data
  @JsProperty
  String message = "Hello VueComponent GWT!";
}