package com.axellience.vuegwt.client.jsnative;

import jsinterop.annotations.JsMethod;

/**
 * Source: https://github.com/ltearno/angular2-gwt/
 */
public class JsTools
{
    @JsMethod(namespace = "console", name = "log")
    public static native void log(String message);

    @JsMethod(namespace = "axellience", name = "propertyInObject")
    public static native boolean propertyInObject(String property, Object object);

    @JsMethod(namespace = "axellience", name = "getObjectProperty")
    public static native Object getObjectProperty(Object object, String property);

    @JsMethod(namespace = "axellience", name = "setObjectProperty")
    public static native void setObjectProperty(Object object, String property, Object value);

    @JsMethod(namespace = "axellience", name = "getArrayItem")
    public static native <T> T getArrayItem(Object array, int index);

    @JsMethod(namespace = "axellience", name = "setArrayItem")
    public static native void setArrayItem(Object array, int index, Object value);

    @JsMethod(namespace = "axellience", name = "getObjectIterator")
    public static native <T> JsIterator<T> getObjectIterator(Object object);

    @JsMethod(namespace = "axellience", name = "convertObject")
    public static native <T> T convertObject(String prototypeName, Object template);

    @JsMethod(namespace = "window.history", name = "back")
    public static native void historyGoBack();

    public static <T> T get(Object o, int index)
    {
        return getArrayItem(o, index);
    }

    public static <T> void set(Object o, int index, T value)
    {
        setArrayItem(o, index, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Object o, String propertyName)
    {
        return (T) getObjectProperty(o, propertyName);
    }

    public static void set(Object o, String propertyName, Object value)
    {
        setObjectProperty(o, propertyName, value);
    }

}
