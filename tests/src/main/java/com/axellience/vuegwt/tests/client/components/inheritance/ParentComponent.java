package com.axellience.vuegwt.tests.client.components.inheritance;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsMethod;

@Component(hasTemplate = false)
public class ParentComponent implements IsVueComponent {

  @Data
  protected String parentData = "PARENT_INITIAL_DATA";

  @Computed
  protected String parentComputed() {
    return "HELLO_FROM_PARENT";
  }

  @JsMethod
  public void setParentData(String parentData) {
    this.parentData = parentData;
  }
}
