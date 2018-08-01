package com.axellience.vuegwt.core.client.component;

import com.axellience.vuegwt.core.annotations.component.Component;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;

/**
 * Every Vue Component should be annotated by {@link Component} and implement this interface
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "?")
public interface IsVueComponent {

  @JsOverlay
  default VueComponent vue() {
    return Js.cast(this);
  }
}
