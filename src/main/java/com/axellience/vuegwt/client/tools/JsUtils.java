package com.axellience.vuegwt.client.tools;

import elemental2.core.Array;
import elemental2.core.Function;
import elemental2.core.JsString;
import elemental2.core.RegExp;
import jsinterop.annotations.JsIgnore;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

import java.util.Collection;
import java.util.Map;

import static jsinterop.base.Js.cast;

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

    public static <T> T getDeepValue(Object object, String path)
    {
        JsPropertyMap objectMap = (JsPropertyMap) object;
        String[] pathSplit = path.split("\\.");
        for (String s : pathSplit)
        {
            objectMap = (JsPropertyMap) objectMap.get(s);
        }
        return (T) objectMap;
    }

    public static Object call(Object method, Object thisArg, Object... args)
    {
        return ((Function) method).apply(thisArg, args);
    }

    public static Object createFunction(String functionBody)
    {
        return new Function(functionBody);
    }

    public static String getFunctionBody(Object jsFunction)
    {
        JsString jsString = cast(jsFunction.toString());

        // Get content between first { and last }
        JsString m = cast(jsString.match(new RegExp("\\{([\\s\\S]*)\\}", "m"))[1]);
        // Strip comments
        return m.replace(new RegExp("^\\s*\\/\\/.*$", "mg"), "").trim();
    }

    @SuppressWarnings("unchecked")
    @JsIgnore
    public static <T> T get(Object o, String propertyName)
    {
        return (T) ((JsPropertyMap) o).get(propertyName);
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
