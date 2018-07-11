package com.axellience.vuegwt.tests.client.components.vmodel;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsMethod;

@Component
public class VModelComponent implements IsVueComponent {

  @Data
  String inputTextValue = "initialValue";

  @Data
  String inputTextValue$WithDollar = "initialValue";

  @JsMethod
  public String getInputTextValue() {
    return inputTextValue;
  }

  @JsMethod
  public void setInputTextValue(String inputTextValue) {
    this.inputTextValue = inputTextValue;
  }

  @JsMethod
  public String getInputTextValue$WithDollar() {
    return inputTextValue$WithDollar;
  }

  @JsMethod
  public void setInputTextValue$WithDollar(String inputTextValue$WithDollar) {
    this.inputTextValue$WithDollar = inputTextValue$WithDollar;
  }
}
