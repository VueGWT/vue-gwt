package com.axellience.vuegwtexamples.client.examples.simplelink;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsProperty;

@Component
public class SimpleLinkComponent implements IsVueComponent {

  @Data
  @JsProperty
  String linkName = "Hello Vue GWT!";
}