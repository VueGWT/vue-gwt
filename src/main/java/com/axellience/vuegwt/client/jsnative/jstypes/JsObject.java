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
    public final <K> K get(String propertyName)
    {
        @SuppressWarnings("unchecked") K result = (K) JsTools.getObjectProperty(this, propertyName);
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

    public static native JsArray<String> getOwnPropertyNames(Object object);
}