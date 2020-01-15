package com.axellience.vuegwt.core.client.customelement;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = "<global>", name = "Object")
public class VueCustomElementType<T extends IsVueComponent> {

  @JsOverlay
  public final VueCustomElement<T> create() {
    return VCE.createElement(this);
  }

  @JsType(isNative = true, namespace = "<global>", name = "VueCustomElement")
  private static final class VCE {
    @JsMethod
    private native static <T extends IsVueComponent> VueCustomElement<T> createElement(
            VueCustomElementType<T> type);
  }
}
