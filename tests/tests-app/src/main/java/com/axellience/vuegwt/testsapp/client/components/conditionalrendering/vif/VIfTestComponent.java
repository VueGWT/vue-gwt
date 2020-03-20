package com.axellience.vuegwt.testsapp.client.components.conditionalrendering.vif;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsMethod;

@Component
public class VIfTestComponent implements IsVueComponent {

  @Data
  boolean ifCondition = false;

  @Data
  boolean elseIfCondition = false;

  @JsMethod
  public void setIfCondition(boolean ifCondition) {
    this.ifCondition = ifCondition;
  }

  @JsMethod
  public void setElseIfCondition(boolean elseIfCondition) {
    this.elseIfCondition = elseIfCondition;
  }
}
