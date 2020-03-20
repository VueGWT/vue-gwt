package com.axellience.vuegwt.testsapp.client.components.slots;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.testsapp.client.common.SimpleObject;
import jsinterop.annotations.JsMethod;

@Component
public class SlotScopeChildComponent implements IsVueComponent {

  @Data
  int myInt = 10;

  @Data
  Integer myInteger = 10;

  @Data
  String myString = "MY_STRING";

  @Data
  SimpleObject mySimpleObject = new SimpleObject("MY_VALUE");

  @Data
  byte myByte = 1;

  @Data
  short myShort = 1;

  @Data
  long myLong = 1L;

  @Data
  float myFloat = 0.5f;

  @Data
  double myDouble = 0.5d;

  @Data
  boolean myBoolean = false;

  @Data
  char myChar = 'a';

  @JsMethod
  public void changeSimpleObjectValue(String newValue)
  {
    mySimpleObject.setStringProperty(newValue);
  }
}
