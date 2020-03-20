package com.axellience.vuegwt.testsapp.client.components.basic.ref;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.annotations.component.Ref;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import elemental2.core.JsArray;
import elemental2.dom.HTMLDivElement;
import java.util.Arrays;
import java.util.List;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

@Component(components = RefChildTestComponent.class)
public class RefParentTestComponent implements IsVueComponent, HasCreated {

  @Data
  @JsProperty
  List<String> list;

  @Ref
  HTMLDivElement divElement;

  @Ref
  JsArray<HTMLDivElement> divElements;

  @Ref
  RefChildTestComponent child;

  @Ref
  JsArray<RefChildTestComponent> children;

  @Override
  public void created() {
    list = Arrays.asList("Hello", "World");
  }

  @JsMethod
  HTMLDivElement getDivElement() {
    return divElement;
  }

  @JsMethod
  JsArray<HTMLDivElement> getDivElements() {
    return divElements;
  }

  @JsMethod
  RefChildTestComponent getChild() {
    return child;
  }

  @JsMethod
  JsArray<RefChildTestComponent> getChildren() {
    return children;
  }
}
