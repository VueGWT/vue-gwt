package com.axellience.vuegwt.client.jsnative.types;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * Basic native wrapper for JSON util object
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class JSON
{
    public static native String stringify(Object object);

    public static native JsObject parse(String string);
}
