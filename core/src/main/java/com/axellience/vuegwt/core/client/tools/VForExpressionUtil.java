package com.axellience.vuegwt.core.client.tools;

import elemental2.core.JsArray;
import java.util.Collection;
import java.util.Map;
import jsinterop.base.Any;
import jsinterop.base.Js;

public class VForExpressionUtil {

  public static <T> Any vForExpressionFromJava(Collection<T> collection) {
    return Js.asAny(JsUtils.arrayFrom(collection));
  }

  public static <K, V> Any vForExpressionFromJava(Map<K, V> collection) {
    return Js.asAny(JsUtils.arrayFrom(collection));
  }

  public static <T> Any vForExpressionFromJava(JsArray<T> collection) {
    return Js.asAny(JsUtils.arrayFrom(collection));
  }

  public static <T> Any vForExpressionFromJava(T[] collection) {
    return Js.asAny(JsUtils.arrayFrom(collection));
  }

  public static Any vForExpressionFromJava(int value) {
    return Js.asAny(value);
  }

  public static Any vForExpressionFromJava(double value) {
    return Js.asAny(value);
  }

  public static Any vForExpressionFromJava(float value) {
    return Js.asAny(value);
  }

  public static Any vForExpressionFromJava(short value) {
    return Js.asAny(value);
  }

  public static Any vForExpressionFromJava(long value) {
    return Js.asAny(value);
  }
}
