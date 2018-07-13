package com.axellience.vuegwt.core.client.observer;

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
}
