package com.axellience.vuegwt.tests.client.components.basic.propdefault;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsProperty;

@Component(components = PropDefaultTestComponent.class)
public class PropDefaultParentTestComponent implements IsVueComponent {

  @JsProperty
  String propParent = "parent value";
}
