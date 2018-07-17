package com.axellience.vuegwt.tests.client.components.basic.data;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.tests.client.common.SimpleObject;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

@Component
public class DataTestComponent implements IsVueComponent {

  @Data
  byte byteData = 0;

  @Data
  short shortData = 0;

  @Data
  int intData = 0;

  @Data
  long longData = 0L;

  @Data
  float floatData = 0f;

  @Data
  double doubleData = 0d;

  @Data
  boolean booleanData = false;

  @Data
  char charData = 'a';

  @Data
  char[] charArrayData = new char[]{'a', 'b', 'c'};

  @Data
  Byte byteObjectData = 0;

  @Data
  Short shortObjectData = 0;

  @Data
  Integer integerData = 0;

  @Data
  Long longObjectData = 0L;

  @Data
  Float floatObjectData = 0f;

  @Data
  Double doubleObjectData = 0d;

  @Data
  Character characterData = 'a';

  @Data
  String stringData = null;

  @Data
  SimpleObject simpleObjectData = null;

  @Data
  String attributeValueData = null;

  @Data
  SimpleObject dataWithLineBreaks = new SimpleObject();

  @Data
  @JsProperty
  String $dataStartingWithDollar = "Hello";

  @Data
  @JsProperty
  String _dataStartingWithUnderscore = "Hello";

  @JsMethod
  public void initSimpleObject() {
    simpleObjectData = new SimpleObject();
  }

  @JsMethod
  public void assignVToCharData() {
    charData = 'v';
  }

  @JsMethod
  public void setByteData(byte byteData) {
    this.byteData = byteData;
  }

  @JsMethod
  public void setShortData(short shortData) {
    this.shortData = shortData;
  }

  @JsMethod
  public void setIntData(int intData) {
    this.intData = intData;
  }

  @JsMethod
  public void setLongData(long longData) {
    this.longData = longData;
  }

  @JsMethod
  public void setFloatData(float floatData) {
    this.floatData = floatData;
  }

  @JsMethod
  public void setDoubleData(double doubleData) {
    this.doubleData = doubleData;
  }

  @JsMethod
  public void setBooleanData(boolean booleanData) {
    this.booleanData = booleanData;
  }

  @JsMethod
  public void setCharData(char charData) {
    this.charData = charData;
  }

  @JsMethod
  public void setCharArrayData(char[] charArrayData) {
    this.charArrayData = charArrayData;
  }

  @JsMethod
  public void setByteObjectData(Byte byteObjectData) {
    this.byteObjectData = byteObjectData;
  }

  @JsMethod
  public void setShortObjectData(Short shortObjectData) {
    this.shortObjectData = shortObjectData;
  }

  @JsMethod
  public void setIntegerData(Integer integerData) {
    this.integerData = integerData;
  }

  @JsMethod
  public void setLongObjectData(Long longObjectData) {
    this.longObjectData = longObjectData;
  }

  @JsMethod
  public void setFloatObjectData(Float floatObjectData) {
    this.floatObjectData = floatObjectData;
  }

  @JsMethod
  public void setDoubleObjectData(Double doubleObjectData) {
    this.doubleObjectData = doubleObjectData;
  }

  @JsMethod
  public void setCharacterData(Character characterData) {
    this.characterData = characterData;
  }

  @JsMethod
  public void setStringData(String stringData) {
    this.stringData = stringData;
  }

  @JsMethod
  public void setSimpleObjectData(SimpleObject simpleObjectData) {
    this.simpleObjectData = simpleObjectData;
  }

  @JsMethod
  public void setAttributeValueData(String attributeValueData) {
    this.attributeValueData = attributeValueData;
  }

  @JsMethod
  public SimpleObject getSimpleObjectData() {
    return simpleObjectData;
  }

  @JsMethod
  public SimpleObject getDataWithLineBreaks() {
    return dataWithLineBreaks;
  }

  @JsMethod
  public void set$dataStartingWithDollar(String $dataStartingWithDollar) {
    this.$dataStartingWithDollar = $dataStartingWithDollar;
  }

  @JsMethod
  public void set_dataStartingWithUnderscore(String _dataStartingWithUnderscore) {
    this._dataStartingWithUnderscore = _dataStartingWithUnderscore;
  }
}
