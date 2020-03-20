package com.axellience.vuegwt.testsapp.client.components.events.emitannotation;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Emit;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsMethod;

@Component
public class EmitAnnotationChild implements IsVueComponent {

  @Emit
  @JsMethod
  public void myEvent() {

  }

  @Emit
  @JsMethod
  public void myEventWithValue(int value) {

  }

  @Emit
  @JsMethod
  public String myEventWithValueAndReturnValue(int value) {
    return "Random Value";
  }

  @Emit("custom-event")
  @JsMethod
  public void myEventWithValueAndCustomName(int value) {

  }

  @Emit
  public void noJsMethodAnnotation(int value) {

  }

  @JsMethod
  public void callEmitMethodFromJava(int value) {
    myEventWithValue(value);
  }

  @JsMethod
  public void callNoJsMethodAnnotation(int value) {
    noJsMethodAnnotation(value);
  }
}
