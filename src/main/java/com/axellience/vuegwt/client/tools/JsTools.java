package com.axellience.vuegwt.client.tools;

import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsType;

/**
 * Source: https://github.com/ltearno/angular2-gwt/
 */
@JsType(namespace = "VueGWT", name = "jsTools")
public class JsTools
{
    @JsMethod(namespace = "console")
    public static native void log(String message);

    @JsMethod(namespace = "console")
    public static native void log(Object object);

    public static native JsObject getWindow();

    public static native boolean objectHasProperty(Object object, String property);

    public static native Object getObjectProperty(Object object, String property);

    public static native void setObjectProperty(Object object, String property, Object value);

    public static native void setObjectProperty(Object object, String property, boolean value);

    public static native void setObjectProperty(Object object, String property, byte value);

    public static native void setObjectProperty(Object object, String property, char value);

    public static native void setObjectProperty(Object object, String property, float value);

    public static native void setObjectProperty(Object object, String property, int value);

    public static native void setObjectProperty(Object object, String property, short value);

    public static native void setObjectProperty(Object object, String property, double value);

    public static native void unsetObjectProperty(Object object, String property);

    public static native <T> T getDeepValue(Object object, String path);

    public static native <T> T getArrayItem(Object array, int index);

    public static native void setArrayItem(Object array, int index, Object value);

    public static native Object call(Object method, Object thisArg, Object... args);

    @JsIgnore
    public static <T> T get(Object o, int index)
    {
        return getArrayItem(o, index);
    }

    @JsIgnore
    public static <T> void set(Object o, int index, T value)
    {
        setArrayItem(o, index, value);
    }

    @SuppressWarnings("unchecked")
    @JsIgnore
    public static <T> T get(Object o, String propertyName)
    {
        return (T) getObjectProperty(o, propertyName);
    }

    @JsIgnore
    public static void set(Object o, String propertyName, Object value)
    {
        setObjectProperty(o, propertyName, value);
    }
}
