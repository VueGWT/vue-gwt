package com.axellience.vuegwt.core.client.tools;

import jsinterop.annotations.JsFunction;

/**
 * @author Adrien Baron
 */
@FunctionalInterface
@JsFunction
public interface AfterMethodCall<T> {

  void execute(T object, String methodName, Object result, Object[] arguments);
}
