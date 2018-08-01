package com.axellience.vuegwt.core.client.observer;

import com.axellience.vuegwt.core.client.observer.functions.VueWalk;
import elemental2.core.Function;
import elemental2.core.JsArray;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class VueObserver {

  @JsProperty
  private Dep dep;

  @JsProperty
  public Proto __proto__;

  public native <T> void observeArray(JsArray<T> array);

  public native void observeArray(Object[] array);

  @JsOverlay
  public final void observe(Object toObserve) {
    observeArray(new JsArray<>(toObserve));
  }

  @JsOverlay
  public final void notifyDep() {
    this.dep.notifySelf();
  }

  private static class Dep {

    @JsMethod(name = "notify")
    public native void notifySelf();
  }

  @JsOverlay
  public final Function getObserveArray() {
    return this.__proto__.observeArray;
  }

  @JsOverlay
  public final Function getWalk() {
    return this.__proto__.walk;
  }

  @JsOverlay
  public final void setWalk(VueWalk walk) {
    this.__proto__.walk = (Function) walk;
  }

  @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
  private static class Proto {
    public Function observeArray;
    public Function walk;
  }
}
