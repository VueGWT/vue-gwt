package com.axellience.vuegwt.client;

import jsinterop.annotations.JsFunction;

/**
 * Callback when a Vue.JS event is fired by a child component
 */
@JsFunction
@FunctionalInterface
public interface OnEvent
{
    void exec(Object parameter);
}
