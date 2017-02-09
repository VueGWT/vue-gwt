package com.axellience.vuegwt.client.jsnative;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import java.util.Collection;
import java.util.Map;

/**
 * @author Adrien Baron
 */
@JsType(name = "VueGwtJavaTools", namespace = JsPackage.GLOBAL)
public class VueGwtJavaTools
{
    public static Object[] collectionToJsArray(Object collection)
    {
        if (collection instanceof Collection<?>)
        {
            return ((Collection) collection).toArray();
        }

        if (collection instanceof Map<?, ?>)
        {
            return ((Map) collection).values().toArray();
        }

        return null;
    }
}
