package com.axellience.vuegwt.core.client.tools;

import elemental2.core.JsArray;
import java.util.Collection;
import java.util.Map;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

public class JsUtils {

  @SafeVarargs
  public static <T> JsPropertyMap<T> map(JsObjectEntry<T>... entries) {
    JsPropertyMap<T> jsObject = (JsPropertyMap<T>) JsPropertyMap.of();
    for (JsObjectEntry<T> entry : entries) {
      jsObject.set(entry.getKey(), entry.getValue());
    }

    return jsObject;
  }

  public static <T> JsPropertyMap<T> map(String key, T value) {
    return (JsPropertyMap<T>) JsPropertyMap.of(key, value);
  }

  public static <T> JsObjectEntry<T> e(String key, T value) {
    return new JsObjectEntry<>(key, value);
  }

  @SafeVarargs
  public static <T> JsArray<T> array(T... array) {
    return new JsArray<>(array);
  }

  public static <T> JsArray<T> arrayFrom(Collection<T> collection) {
    return Js.cast(collection.toArray());
  }

  public static <K, V> JsArray<V> arrayFrom(Map<K, V> map) {
    return Js.cast(map.values().toArray());
  }

  public static <T> JsArray<T> arrayFrom(JsArray<T> array) {
    return array;
  }

  public static <T> JsArray<T> arrayFrom(T[] array) {
    return Js.cast(array);
  }

  public static class JsObjectEntry<T> {

    private String key;
    private T value;

    JsObjectEntry(String key, T value) {
      this.key = key;
      this.value = value;
    }

    public String getKey() {
      return key;
    }

    public T getValue() {
      return value;
    }
  }
}
