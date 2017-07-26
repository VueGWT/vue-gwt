package com.axellience.vuegwtexamples.client.examples.extendsjscomponent.parent;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Computed;
import com.axellience.vuegwt.jsr69.component.annotations.JsComponent;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsComponent
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class ParentJsComponent extends VueComponent
{
    public String parentMessage;

    public native int parentMultiplyBy2(int value);

    @Override
    public native void created();

    @Computed
    public native String getParentComputed();
}
