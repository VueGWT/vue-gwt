package com.axellience.vuegwt.core.client.component;

import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public interface IsVueComponent
{
    @JsOverlay
    default VueComponent asVue() {
        return Js.cast(this);
    }
}
