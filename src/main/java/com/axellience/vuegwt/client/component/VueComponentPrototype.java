package com.axellience.vuegwt.client.component;

import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * The Java representation of a Vue Component.
 * Whenever you want to add a component to your application you should extends this class and add
 * the {@link Component} annotation.
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public abstract class VueComponentPrototype<T extends VueComponent> extends JsObject
{
}