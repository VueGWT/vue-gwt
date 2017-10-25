package com.axellience.vuegwt.client.component;

import elemental2.core.Function;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Function")
public class ComponentJavaConstructor<T extends VueComponent> extends Function
{
    public ComponentJavaPrototype<T> prototype;
}
