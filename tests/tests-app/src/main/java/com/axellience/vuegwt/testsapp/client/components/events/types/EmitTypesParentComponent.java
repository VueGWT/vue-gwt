package com.axellience.vuegwt.testsapp.client.components.events.types;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.testsapp.client.common.Todo;
import jsinterop.annotations.JsMethod;

@Component(components = {EmitTypesChildComponent.class})
public class EmitTypesParentComponent implements IsVueComponent {

  private int myInt; // 10
  private boolean myBoolean; // false
  private double myDouble; // 12
  private float myFloat; // 12.5
  private Todo myTodo; // "Hello World"
  private Integer myInteger; // 10

  private int myIntAsAny; // 10
  private boolean myBooleanAsAny; // false
  private double myDoubleAsAny; // 12
  private float myFloatAsAny; // 12.5
  private Todo myTodoAsAny; // "Hello World"
  private Integer myIntegerAsAny; // 10

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

  @JsMethod
  public int getMyIntAsAny() {
    return myIntAsAny;
  }

  @JsMethod
  public void setIntAsAny(int myIntAsAny) {
    this.myIntAsAny = myIntAsAny;
  }

  @JsMethod
  public boolean getMyBooleanAsAny() {
    return myBooleanAsAny;
  }

  @JsMethod
  public void setBooleanAsAny(boolean myBooleanAsAny) {
    this.myBooleanAsAny = myBooleanAsAny;
  }

  @JsMethod
  public double getMyDoubleAsAny() {
    return myDoubleAsAny;
  }

  @JsMethod
  public void setDoubleAsAny(double myDoubleAsAny) {
    this.myDoubleAsAny = myDoubleAsAny;
  }

  @JsMethod
  public float getMyFloatAsAny() {
    return myFloatAsAny;
  }

  @JsMethod
  public void setFloatAsAny(float myFloatAsAny) {
    this.myFloatAsAny = myFloatAsAny;
  }

  @JsMethod
  public Todo getMyTodoAsAny() {
    return myTodoAsAny;
  }

  @JsMethod
  public void setTodoAsAny(Todo myTodoAsAny) {
    this.myTodoAsAny = myTodoAsAny;
  }

  @JsMethod
  public Integer getMyIntegerAsAny() {
    return myIntegerAsAny;
  }

  @JsMethod
  public void setIntegerAsAny(Integer myIntegerAsAny) {
    this.myIntegerAsAny = myIntegerAsAny;
  }
}
