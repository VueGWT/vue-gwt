package com.axellience.vuegwt.tests.client.components.basic.prop;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.tests.client.common.SimpleObject;
import jsinterop.annotations.JsProperty;

@Component(components = PropTestComponent.class)
public class PropParentTestComponent implements IsVueComponent {

  @JsProperty
  int optionalPropParent = 6;

  @JsProperty
  SimpleObject requiredPropParent = new SimpleObject();
}
