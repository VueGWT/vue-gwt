package com.axellience.vuegwt.tests.client.components.basic.computed;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsProperty;

@Component
public class ComputedTestComponent implements IsVueComponent {

  @JsProperty
  String data = null;

  @Computed
  public String getComputedData() {
    return data == null ? null : "#" + data + "#";
  }
}
