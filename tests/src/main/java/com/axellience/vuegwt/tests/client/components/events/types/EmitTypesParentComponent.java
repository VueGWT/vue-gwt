package com.axellience.vuegwt.tests.client.components.events.types;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.tests.client.common.Todo;
import jsinterop.annotations.JsMethod;

@Component(components = {EmitTypesChildComponent.class})
public class EmitTypesParentComponent implements IsVueComponent {

  @Data
  public int myInt; // 10
  @Data
  public boolean myBoolean; // false
  @Data
  public double myDouble; // 12
  @Data
  public float myFloat; // 12.5
  @Data
  public Todo myTodo; // "Hello World"

  private Integer myInteger; // 10

  @JsMethod
  public void setInt(int eventInt) {
    this.myInt = eventInt;
  }

  @JsMethod
  public void setInteger(Integer eventInteger) {
    this.myInteger = eventInteger;
  }

  @JsMethod
  public void setBoolean(boolean eventBoolean) {
    this.myBoolean = eventBoolean;
  }

  @JsMethod
  public void setDouble(double eventDouble) {
    this.myDouble = eventDouble;
  }

  @JsMethod
  public void setFloat(float eventFloat) {
    this.myFloat = eventFloat;
  }

  @JsMethod
  public void setTodo(Todo eventTodo) {
    this.myTodo = eventTodo;
  }

  @JsMethod
  public Integer getMyInteger() {
    return myInteger;
  }

  @JsMethod
  public Integer getTestIntegerValue() {
    return 10;
  }

  @JsMethod
  public int getMyInt() {
    return myInt;
  }

  @JsMethod
  public boolean getMyBoolean() {
    return myBoolean;
  }

  @JsMethod
  public double getMyDouble() {
    return myDouble;
  }

  @JsMethod
  public float getMyFloat() {
    return myFloat;
  }

  @JsMethod
  public Todo getMyTodo() {
    return myTodo;
  }
}
