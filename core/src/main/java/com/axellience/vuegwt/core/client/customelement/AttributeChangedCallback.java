package com.axellience.vuegwt.core.client.customelement;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsFunction;

@FunctionalInterface
@JsFunction
public interface AttributeChangedCallback<T extends IsVueComponent> {

  void onAttributeChange(VueCustomElement<T> element, String name, Object oldValue,
      Object newValue);
}
