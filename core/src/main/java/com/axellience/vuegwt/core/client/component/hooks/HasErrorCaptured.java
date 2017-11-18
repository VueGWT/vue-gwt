package com.axellience.vuegwt.core.client.component.hooks;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.annotations.component.HookMethod;
import jsinterop.annotations.JsMethod;
import jsinterop.base.JsPropertyMap;

public interface HasErrorCaptured
{
    @HookMethod
    @JsMethod
    boolean errorCaptured(JsPropertyMap err, VueComponent vue, String info);
}
