package com.axellience.vuegwt.tests.client.components.style.scoped;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsMethod;

@Component
public class ScopedTestComponent implements IsVueComponent {

  @Prop
  String celsius;

  @JsMethod
  double parseFloat(String value) {
    if (value == null) {
      return 0;
    }
    String s = value.trim();
    if (s.isEmpty()) {
      return 0;
    }
    try {
      return Double.parseDouble(s);
    } catch (Exception e) {
      return 0;
    }
  }
}