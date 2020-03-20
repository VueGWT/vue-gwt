package com.axellience.vuegwt.testsapp.client.components.conditionalrendering.vshow;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsMethod;

@Component
public class VShowTestComponent implements IsVueComponent {

  @Data
  boolean showCondition = false;

  @JsMethod
  public void setShowCondition(boolean showCondition) {
    this.showCondition = showCondition;
  }
}
