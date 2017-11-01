package com.axellience.vuegwt.core.client.component.options.functions;

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
