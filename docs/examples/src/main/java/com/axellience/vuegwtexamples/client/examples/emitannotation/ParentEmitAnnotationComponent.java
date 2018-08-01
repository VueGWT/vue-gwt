package com.axellience.vuegwtexamples.client.examples.emitannotation;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import elemental2.dom.DomGlobal;
import jsinterop.annotations.JsMethod;

@Component(components = EmitAnnotationComponent.class)
public class ParentEmitAnnotationComponent implements IsVueComponent {

  @JsMethod
  public void log(String eventName, Object eventValue) {
    DomGlobal.console.log(eventName, eventValue);
  }
}
