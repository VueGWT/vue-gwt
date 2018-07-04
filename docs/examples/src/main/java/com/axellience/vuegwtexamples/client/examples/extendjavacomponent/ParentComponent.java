package com.axellience.vuegwtexamples.client.examples.extendjavacomponent;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsMethod;

/**
 * @author Adrien Baron
 */
@Component
public abstract class ParentComponent implements IsVueComponent {

  @Data
  String parentMessage = "This is a message from the parent";

  @JsMethod
  public int parentMultiplyBy2(int value) {
    return value * 2;
  }

  @Computed
  public String getParentComputed() {
    return "Computed Message | " + this.parentMessage;
  }
}
