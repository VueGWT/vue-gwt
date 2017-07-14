package com.axellience.vuegwt.client.jsnative.jsfunctions;

import jsinterop.annotations.JsFunction;

/**
 * Can be used when we have a simple callback with no parameters and no return
 */
@JsFunction
@FunctionalInterface
public interface JsSimpleFunction
{
    void execute();
}