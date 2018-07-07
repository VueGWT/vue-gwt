package com.axellience.vuegwt.tests.client.components.basic.computed;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsMethod;

@Component
public class ComputedTestComponent implements IsVueComponent {

  @Data
  String data = null;

  @Computed
  public String getComputedProperty() {
    return data == null ? null : "#" + data + "#";
  }

  @Computed
  public String computedPropertyNoGet() {
    return data == null ? null : "!" + data + "!";
  }

  @JsMethod
  public void setData(String data) {
    this.data = data;
  }
}
