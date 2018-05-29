package com.axellience.vuegwt.tests.client.components.basic.propvalidator;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsProperty;

@Component(components = PropValidatorTestComponent.class)
public class PropValidatorParentTestComponent implements IsVueComponent {

  @JsProperty
  int validatedPropParent = 0;
}
