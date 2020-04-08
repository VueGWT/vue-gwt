package com.axellience.vuegwt.testsapp.client.components.basic.prop;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasMounted;
import com.axellience.vuegwt.testsapp.client.common.SimpleObject;
import elemental2.core.JsArray;
import jsinterop.annotations.JsMethod;

@Component(components = PropTestComponent.class)
public class PropParentTestComponent implements IsVueComponent, HasMounted {

  @Data
  int optionalPropParent = 6;

  @Data
  SimpleObject requiredPropParent = new SimpleObject();

  @Data
  JsArray<String> arrayData = new JsArray<>();

  SimpleObject nonObservedObject = new SimpleObject();

  @Override
  public void mounted() {
    arrayData.push("value-1", "value-2");
  }

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
