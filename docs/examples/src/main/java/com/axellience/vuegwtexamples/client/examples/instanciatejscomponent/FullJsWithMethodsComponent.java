package com.axellience.vuegwtexamples.client.examples.instanciatejscomponent;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.annotations.component.JsComponent;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsComponent("FullJsWithMethodsComponent")
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Function")
public class FullJsWithMethodsComponent implements IsVueComponent
{
    public int value;
    public native int multiplyBy2(int value);
}
