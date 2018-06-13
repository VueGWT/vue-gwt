package com.axellience.vuegwtexamples.client.examples.canhide;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsProperty;

@Component
public class CanHideComponent implements IsVueComponent {

  @JsProperty
  boolean visible = true;
}