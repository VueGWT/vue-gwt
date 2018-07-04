package com.axellience.vuegwt.tests.client.components.basic.propvalidator;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsMethod;

@Component(components = PropValidatorTestComponent.class)
public class PropValidatorParentTestComponent implements IsVueComponent {

  @Data
  int validatedPropParent = 0;

  @JsMethod
  public void setValidatedPropParent(int validatedPropParent) {
    this.validatedPropParent = validatedPropParent;
  }
}
