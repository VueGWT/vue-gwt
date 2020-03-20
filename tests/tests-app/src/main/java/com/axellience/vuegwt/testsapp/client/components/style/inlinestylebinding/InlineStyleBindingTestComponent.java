package com.axellience.vuegwt.testsapp.client.components.style.inlinestylebinding;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsMethod;

@Component
public class InlineStyleBindingTestComponent implements IsVueComponent {

  @Data
  String color = "black";

  @Data
  int fontSize = 12;

  @JsMethod
  public void setColor(String color) {
    this.color = color;
  }

  @JsMethod
  public void setFontSize(int fontSize) {
    this.fontSize = fontSize;
  }
}
