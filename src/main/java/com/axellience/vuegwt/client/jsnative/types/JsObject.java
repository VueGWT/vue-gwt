package com.axellience.vuegwt.client.jsnative.types;

import com.axellience.vuegwt.client.jsnative.JsTools;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * Source: https://github.com/ltearno/angular2-gwt/
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class JsObject
{
    @JsOverlay
    public final <T> T get(String propertyName)
    {
        @SuppressWarnings("unchecked") T result = (T) JsTools.getObjectProperty(this, propertyName);
        return result;
    }

    @JsOverlay
    public final JsObject set(String propertyName, Object value)
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

    public static native JsArray<String> getOwnPropertyNames(Object object);
}