package com.axellience.vuegwtexamples.client.examples.extendsjscomponent;

import com.axellience.vuegwt.client.Vue;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class ChildJsComponent extends Vue
{
    @JsProperty
    protected String message;

    @JsMethod
    protected native int multiplyBy2(int myNumber);

    @Override
    public native void created();
}
