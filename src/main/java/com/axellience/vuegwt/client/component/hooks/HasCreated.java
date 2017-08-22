package com.axellience.vuegwt.client.component.hooks;

import com.axellience.vuegwt.jsr69.component.annotations.HookMethod;
import jsinterop.annotations.JsMethod;

/**
 * @author Adrien Baron
 */
public interface HasCreated
{
    @HookMethod
    @JsMethod
    void created();
}
