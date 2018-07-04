package com.axellience.vuegwtexamples.client.examples.counterwithevent;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsMethod;

/**
 * @author Adrien Baron
 */
@Component(components = ButtonCounterComponent.class)
public class CounterWithEventComponent implements IsVueComponent {

  @Data
  protected int total = 0;

  @JsMethod
  protected void incrementTotal() {
    this.total++;
  }
}
