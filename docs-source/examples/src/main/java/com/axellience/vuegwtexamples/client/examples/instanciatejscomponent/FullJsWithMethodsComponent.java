package com.axellience.vuegwtexamples.client.examples.instanciatejscomponent;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.annotations.component.JsComponent;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsComponent("FullJsWithMethodsComponent")
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Function")
public class FullJsWithMethodsComponent extends VueComponent
{
    public int value;
    public native int multiplyBy2(int value);
}
