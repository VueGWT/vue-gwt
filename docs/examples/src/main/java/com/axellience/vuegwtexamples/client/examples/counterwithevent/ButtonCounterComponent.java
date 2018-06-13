package com.axellience.vuegwtexamples.client.examples.counterwithevent;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class ButtonCounterComponent implements IsVueComponent {

  @JsProperty
  protected int counter = 0;

  @JsMethod
  protected void increment() {
    this.counter++;
    vue().$emit("increment");
  }
}
