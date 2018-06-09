package com.axellience.vuegwt.tests.client.components.conditionalrendering.vshow;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsProperty;

@Component
public class VShowTestComponent implements IsVueComponent {

  @JsProperty
  boolean showCondition = false;
}
