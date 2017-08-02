package com.axellience.vuegwt.client.component;

import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * The Java representation of a {@link VueComponent} JS prototype.
 * Vue.js components will extend this prototype to get access to private Java methods.
 * It will also allow GWT to recognize them as instance of the Java {@link VueComponent}.
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public abstract class VueComponentPrototype<T extends VueComponent> extends JsObject
{
}