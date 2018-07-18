package com.axellience.vuegwt.core.client.tools;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class FieldsExposer {
  public static native <T> T v();
  public static native void e(Object... values);
}
