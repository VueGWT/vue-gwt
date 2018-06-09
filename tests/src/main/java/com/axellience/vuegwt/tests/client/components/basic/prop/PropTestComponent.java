package com.axellience.vuegwt.tests.client.components.basic.prop;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.tests.client.common.SimpleObject;
import jsinterop.annotations.JsProperty;

@Component
public class PropTestComponent implements IsVueComponent {

  @Prop
  @JsProperty
  int optionalProp;

  @Prop(required = true)
  @JsProperty
  SimpleObject requiredProp;
}
