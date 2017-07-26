package com.axellience.vuegwt.client.tools;

import jsinterop.annotations.JsFunction;

/**
 * @author Adrien Baron
 */
@FunctionalInterface
@JsFunction
public interface BeforeMethodCall<T>
{
    void execute(T object, String methodName, Object... arguments);
}
