package com.axellience.vuegwt.client.component;

import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class ComponentJavaPrototype<T extends VueComponent> extends JsObject
{
}
