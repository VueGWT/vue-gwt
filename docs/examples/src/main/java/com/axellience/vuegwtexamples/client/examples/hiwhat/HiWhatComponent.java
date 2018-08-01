package com.axellience.vuegwtexamples.client.examples.hiwhat;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.google.gwt.user.client.Window;
import jsinterop.annotations.JsMethod;

/**
 * @author Adrien Baron
 */
@Component
public class HiWhatComponent implements IsVueComponent {

  @JsMethod
  public void say(String message) {
    Window.alert(message);
  }
}
