package com.axellience.vuegwt.core.client.customelement;

import com.axellience.vuegwt.core.client.component.VueComponent;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = "<global>", name = "Object")
public class VueCustomElementType<T extends VueComponent>
{
    @JsOverlay
    public final VueCustomElement<T> create()
    {
        return createElement(this);
    }

    @JsMethod(namespace = "VueCustomElement")
    private native static <T extends VueComponent> VueCustomElement<T> createElement(
        VueCustomElementType<T> type);
}
