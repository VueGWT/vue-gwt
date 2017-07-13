package com.axellience.vuegwt.client.jsnative.vnode;

import com.axellience.vuegwt.client.jsnative.JsTools;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class Dictionary<T>
{
    @JsOverlay
    public final T get(String propertyName)
    {
        @SuppressWarnings("unchecked") T result = (T) JsTools.getObjectProperty(this, propertyName);
        return result;
    }

    @JsOverlay
    public final Dictionary put(String propertyName, T value)
    {
        JsTools.setObjectProperty(this, propertyName, value);
        return this;
    }
}