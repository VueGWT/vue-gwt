package com.axellience.vuegwt.core.client.observer;

import com.axellience.vuegwt.core.client.observer.functions.VueObserveArray;
import com.axellience.vuegwt.core.client.observer.functions.VueWalk;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class VueObserverPrototype {

  public VueObserveArray observeArray;
  public VueWalk walk;
}
