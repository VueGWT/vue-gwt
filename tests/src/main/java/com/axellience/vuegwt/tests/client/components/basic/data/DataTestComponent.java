package com.axellience.vuegwt.tests.client.components.basic.data;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;

import com.axellience.vuegwt.tests.client.common.SimpleObject;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

@Component
public class DataTestComponent implements IsVueComponent {

  @JsProperty
  byte byteData = 0;

  @JsProperty
  short shortData = 0;

  @JsProperty
  int intData = 0;

  @JsProperty
  long longData = 0L;

  @JsProperty
  float floatData = 0f;

  @JsProperty
  double doubleData = 0d;

  @JsProperty
  boolean booleanData = false;

  @JsProperty
  char charData = 'a';

  @JsProperty
  char[] charArrayData = new char[]{'a', 'b', 'c'};

  @JsProperty
  Byte byteObjectData = 0;

  @JsProperty
  Short shortObjectData = 0;

  @JsProperty
  Integer integerData = 0;

  @JsProperty
  Long longObjectData = 0L;

  @JsProperty
  Float floatObjectData = 0f;

  @JsProperty
  Double doubleObjectData = 0d;

  @JsProperty
  Character characterData = 'a';

  @JsProperty
  String stringData = null;

  @JsProperty
  SimpleObject simpleObjectData = null;

  @JsProperty
  String attributeValueData = null;

  @JsMethod
  public void initSimpleObject() {
    simpleObjectData = new SimpleObject();
  }
}
