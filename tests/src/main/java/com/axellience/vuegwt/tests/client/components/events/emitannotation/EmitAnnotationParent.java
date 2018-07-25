package com.axellience.vuegwt.tests.client.components.events.emitannotation;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsMethod;

@Component(components = EmitAnnotationChild.class)
public class EmitAnnotationParent implements IsVueComponent {

  private boolean myEventReceived;
  private int myEventWithValue;
  private int myEventWithValueAndReturnValue;
  private int customEvent;
  private int noJsMethodAnnotation;


  @JsMethod
  public boolean isMyEventReceived() {
    return myEventReceived;
  }

  @JsMethod
  public void onMyEvent() {
    this.myEventReceived = true;
  }

  @JsMethod
  public int getMyEventWithValue() {
    return myEventWithValue;
  }

  @JsMethod
  public void onMyEventWithValue(int myEventWithValue) {
    this.myEventWithValue = myEventWithValue;
  }

  @JsMethod
  public int getMyEventWithValueAndReturnValue() {
    return myEventWithValueAndReturnValue;
  }

  @JsMethod
  public void onMyEventWithValueAndReturnValue(int myEventWithValueAndReturnValue) {
    this.myEventWithValueAndReturnValue = myEventWithValueAndReturnValue;
  }

  @JsMethod
  public int getCustomEvent() {
    return customEvent;
  }

  @JsMethod
  public void onCustomEvent(int customEvent) {
    this.customEvent = customEvent;
  }

  @JsMethod
  public int getNoJsMethodAnnotation() {
    return noJsMethodAnnotation;
  }

  @JsMethod
  public void onNoJsMethodAnnotation(int noJsMethodAnnotation) {
    this.noJsMethodAnnotation = noJsMethodAnnotation;
  }
}
