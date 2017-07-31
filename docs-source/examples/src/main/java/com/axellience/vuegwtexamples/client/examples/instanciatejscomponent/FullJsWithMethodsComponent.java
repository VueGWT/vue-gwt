package com.axellience.vuegwtexamples.client.examples.instanciatejscomponent;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.JsComponent;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsComponent
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class FullJsWithMethodsComponent extends VueComponent
{
    public int value;

    @Override
    public void created()
    {

    }

    public native int multiplyBy2(int value);
}
