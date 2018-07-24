package com.axellience.vuegwt.tests.client.components.basic.computed;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.tests.client.common.SimpleObject;
import jsinterop.annotations.JsMethod;

@Component
public class ComputedTestComponent implements IsVueComponent {

  @Data
  String data = null;

  @Data
  SimpleObject simpleObject;

  @Computed
  public String getComputedProperty() {
    return data == null ? null : "#" + data + "#";
  }

  @Computed
  public String computedPropertyNoGet() {
    return data == null ? null : "!" + data + "!";
  }

  @Computed
  public String computedNoNullCheck() {
    return simpleObject.getStringProperty();
  }

  @JsMethod
  public void setData(String data) {
    this.data = data;
  }
}
