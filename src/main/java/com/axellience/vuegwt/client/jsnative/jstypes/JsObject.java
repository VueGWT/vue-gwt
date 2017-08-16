package com.axellience.vuegwt.client.jsnative.jstypes;

import com.axellience.vuegwt.client.tools.JsTools;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * Original Source: https://github.com/ltearno/angular2-gwt/
 * Modified by Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class JsObject<T>
{
    @JsOverlay
    public static JsObject of(String key, Object value)
    {
        return new JsObject().set(key, value);
    }

    @JsOverlay
    @SafeVarargs
    public static <T> JsObject<T> map(JsObjectEntry<T>... entries)
    {
        JsObject<T> jsObject = new JsObject<>();
        for (JsObjectEntry<T> entry : entries)
            jsObject.set(entry.getKey(), entry.getValue());

        return jsObject;
    }

    @JsOverlay
    public static <T> JsObjectEntry<T> e(String key, T value)
    {
        return new JsObjectEntry<>(key, value);
    }

    @JsOverlay
    public final T get(String propertyName)
    {
        @SuppressWarnings("unchecked") T result = (T) JsTools.getObjectProperty(this, propertyName);
        return result;
    }

    @JsOverlay
    public final JsObject set(String propertyName, T value)
    {
        JsTools.setObjectProperty(this, propertyName, value);
        return this;
    }

    @JsOverlay
    public final JsObject set(String propertyName, boolean value)
    {
        JsTools.setObjectProperty(this, propertyName, value);
        return this;
    }

    @JsOverlay
    public final JsObject set(String propertyName, byte value)
    {
        JsTools.setObjectProperty(this, propertyName, value);
        return this;
    }

    @JsOverlay
    public final JsObject set(String propertyName, char value)
    {
        JsTools.setObjectProperty(this, propertyName, value);
        return this;
    }

    @JsOverlay
    public final JsObject set(String propertyName, float value)
    {
        JsTools.setObjectProperty(this, propertyName, value);
        return this;
    }

    @JsOverlay
    public final JsObject set(String propertyName, int value)
    {
        JsTools.setObjectProperty(this, propertyName, value);
        return this;
    }

    @JsOverlay
    public final JsObject set(String propertyName, short value)
    {
        JsTools.setObjectProperty(this, propertyName, value);
        return this;
    }

    @JsOverlay
    public final JsObject set(String propertyName, double value)
    {
        JsTools.setObjectProperty(this, propertyName, value);
        return this;
    }

    public native boolean hasOwnProperty(String propertyName);

    public static native JsObject getPrototypeOf(Object object);

    public static native JsArray<String> getOwnPropertyNames(Object object);

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