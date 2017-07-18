package com.axellience.vuegwtexamples.client.examples.instanciatejscomponent;

import com.axellience.vuegwt.client.Vue;
import com.axellience.vuegwt.jsr69.component.annotations.JsComponent;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsComponent
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class FullJsWithMethodsComponent extends Vue
{
    public int value;

    @Override
    public native void created();

    public native int multiplyBy2(int value);
}
