package com.axellience.vuegwt.tests.client.components.basic.propsync;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsProperty;

@Component(components = PropSyncTestComponent.class)
public class PropSyncParentTestComponent implements IsVueComponent {

  @Data
  @JsProperty
  protected String prop = "originalValue";
}
