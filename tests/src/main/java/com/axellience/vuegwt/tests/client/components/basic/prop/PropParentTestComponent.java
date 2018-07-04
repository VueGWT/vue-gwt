package com.axellience.vuegwt.tests.client.components.basic.prop;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.tests.client.common.SimpleObject;
import jsinterop.annotations.JsMethod;

@Component(components = PropTestComponent.class)
public class PropParentTestComponent implements IsVueComponent {

  @Data
  int optionalPropParent = 6;

  @Data
  SimpleObject requiredPropParent = new SimpleObject();

  SimpleObject nonObservedObject = new SimpleObject();

  @JsMethod
  public void setOptionalPropParent(int optionalPropParent) {
    this.optionalPropParent = optionalPropParent;
  }

  @JsMethod
  public SimpleObject getRequiredPropParent() {
    return requiredPropParent;
  }

  @JsMethod
  public SimpleObject getNonObservedObject() {
    return nonObservedObject;
  }
}
