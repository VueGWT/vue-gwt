package com.axellience.vuegwt.client.component.hooks;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.HookMethod;
import jsinterop.annotations.JsMethod;
import jsinterop.base.JsPropertyMap;

public interface HasErrorCaptured
{
    @HookMethod
    @JsMethod
    boolean errorCaptured(JsPropertyMap err, VueComponent vue, String info);
}
