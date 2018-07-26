package com.axellience.vuegwtexamples.client.examples.reverse;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsMethod;

/**
 * @author Adrien Baron
 */
@Component
public class ReverseComponent implements IsVueComponent {

  @Data
  String message = "Hello";

  @Computed
  public String getReversedMessage() {
    return new StringBuilder(message).reverse().toString();
  }

  @JsMethod
  public void setMessage(String message) {
    this.message = message;
  }
}