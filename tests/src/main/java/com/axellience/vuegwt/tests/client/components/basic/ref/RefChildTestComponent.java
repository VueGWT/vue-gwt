package com.axellience.vuegwt.tests.client.components.basic.ref;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsMethod;

@Component
public class RefChildTestComponent implements IsVueComponent {

  @Data
  String data = "Hello";

  @JsMethod
  void setData(String data) {
    this.data = data;
  }
}
