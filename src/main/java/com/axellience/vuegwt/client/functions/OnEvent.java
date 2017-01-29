package com.axellience.vuegwt.client.functions;

import jsinterop.annotations.JsFunction;

/**
 * Callback for Vue events
 * @author Adrien Baron
 */
@JsFunction
@FunctionalInterface
public interface OnEvent
{
    void exec(Object parameter);
}
