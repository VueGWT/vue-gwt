package com.axellience.vuegwt.core.client.observer;

import static jsinterop.base.Js.cast;

import com.axellience.vuegwt.core.client.observer.functions.VueObserveArray;
import com.axellience.vuegwt.core.client.observer.functions.VueWalk;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.JsPropertyMap;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class VueObserverPrototype implements JsPropertyMap<Object> {

  @JsOverlay
  public final VueObserveArray getObserveArray() {
    return cast(get("observeArray"));
  }

  @JsOverlay
  public final VueWalk getWalk() {
    return cast(get("walk"));
  }

  @JsOverlay
  public final void setWalk(VueWalk walk) {
    set("walk", walk);
  }
}
