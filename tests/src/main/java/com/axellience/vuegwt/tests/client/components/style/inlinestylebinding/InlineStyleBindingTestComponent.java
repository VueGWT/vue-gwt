package com.axellience.vuegwt.tests.client.components.style.inlinestylebinding;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsProperty;

@Component
public class InlineStyleBindingTestComponent implements IsVueComponent {

  @JsProperty
  String color = "black";

  @JsProperty
  int fontSize = 12;
}
