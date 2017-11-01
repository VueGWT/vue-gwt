package com.axellience.vuegwt.core.client.jsnative.jsfunctions;

import jsinterop.annotations.JsFunction;

/**
 * Can be used when we have a simple callback with no parameters and no return
 */
@JsFunction
@FunctionalInterface
public interface JsConsumer<T>
{
    void accept(T accept);
}