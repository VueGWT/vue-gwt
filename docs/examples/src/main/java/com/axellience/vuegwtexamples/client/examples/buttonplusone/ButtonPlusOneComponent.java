package com.axellience.vuegwtexamples.client.examples.buttonplusone;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class ButtonPlusOneComponent implements IsVueComponent {

  @JsProperty
  int counter = 0;
}
