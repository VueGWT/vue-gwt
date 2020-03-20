package com.axellience.vuegwt.testsapp.client.components.vmodel;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import elemental2.core.JsString;
import jsinterop.annotations.JsMethod;
import jsinterop.base.Js;

@Component
public class VModelComponent implements IsVueComponent {

  @Data
  String inputTextValue = "initialValue";

  @Data
  String inputTextValue$WithDollar = "initialValue";

  @Data
  String computedInputTextValueProperty = "initialValue";

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

  @Computed
  public String getComputedInputTextValue() {
    return Js.<JsString>uncheckedCast(computedInputTextValueProperty).replace("Value", "Bobby");
  }

  @Computed
  public void setComputedInputTextValue(String computedInputTextValue) {
    this.computedInputTextValueProperty = Js.<JsString>uncheckedCast(computedInputTextValue).replace("Bobby", "Value");
  }

  @JsMethod
  public String getComputedInputTextValueProperty() {
    return computedInputTextValueProperty;
  }

  @JsMethod
  public void setComputedInputTextValueProperty(String computedInputTextValueProperty) {
    this.computedInputTextValueProperty = computedInputTextValueProperty;
  }
}
