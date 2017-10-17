package com.axellience.vuegwt.client.component.hooks;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.jsr69.component.annotations.HookMethod;
import jsinterop.annotations.JsMethod;

public interface HasErrorCaptured
{
    @HookMethod
    @JsMethod
    boolean errorCaptured(JsObject err, VueComponent vue, String info);
}
