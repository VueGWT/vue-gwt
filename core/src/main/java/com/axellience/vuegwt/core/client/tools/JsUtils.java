package com.axellience.vuegwt.core.client.tools;

import elemental2.core.Array;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

import java.util.Collection;
import java.util.Map;

public class JsUtils
{
    @SafeVarargs
    public static <T> JsPropertyMap<T> map(JsObjectEntry<T>... entries)
    {
        JsPropertyMap<T> jsObject = (JsPropertyMap<T>) JsPropertyMap.of();
        for (JsObjectEntry<T> entry : entries)
            jsObject.set(entry.getKey(), entry.getValue());

        return jsObject;
    }

    public static <T> JsPropertyMap<T> map(String key, T value)
    {
        return (JsPropertyMap<T>) JsPropertyMap.of(key, value);
    }

    public static <T> JsObjectEntry<T> e(String key, T value)
    {
        return new JsObjectEntry<>(key, value);
    }

    @SafeVarargs
    public static <T> Array<T> array(T... array)
    {
        return new Array<>(array);
    }

    public static <T> Array<T> from(Collection<T> collection)
    {
        return Js.cast(collection.toArray());
    }

    public static <K, V> Array<V> from(Map<K, V> map)
    {
        return Js.cast(map.values().toArray());
    }

    public static <T> Array<T> from(Array<T> array)
    {
        return array;
    }

    public static <T> Array<T> from(T[] array)
    {
        return Js.cast(array);
    }

    public static class JsObjectEntry<T>
    {
        private String key;
        private T value;

        JsObjectEntry(String key, T value)
        {
            this.key = key;
            this.value = value;
        }

        public String getKey()
        {
            return key;
        }

        public T getValue()
        {
            return value;
        }
    }
}
