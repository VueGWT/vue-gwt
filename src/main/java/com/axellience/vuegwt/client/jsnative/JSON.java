package com.axellience.vuegwt.client.jsnative;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class JSON
{
    public static native String stringify(Object object);

    public static native Object parse(String string);
}
