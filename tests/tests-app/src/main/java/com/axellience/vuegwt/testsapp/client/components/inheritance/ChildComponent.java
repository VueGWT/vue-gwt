package com.axellience.vuegwt.testsapp.client.components.inheritance;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwt.core.annotations.component.Data;
import jsinterop.annotations.JsMethod;

@Component
public class ChildComponent extends ParentComponent {
  @Data
  protected String childData = "CHILD_INITIAL_DATA";

  @Computed
  protected String childComputed() {
    return "HELLO_FROM_CHILD";
  }

  @JsMethod
  public void setChildData(String childData) {
    this.childData = childData;
  }
}
