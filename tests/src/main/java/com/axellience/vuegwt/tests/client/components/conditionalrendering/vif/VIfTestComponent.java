package com.axellience.vuegwt.tests.client.components.conditionalrendering.vif;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsProperty;

@Component
public class VIfTestComponent implements IsVueComponent {

  @JsProperty
  boolean ifCondition = false;

  @JsProperty
  boolean elseIfCondition = false;
}
