package com.axellience.vuegwt.client.jsnative.jstypes.arrayfunctions;

import jsinterop.annotations.JsFunction;

/**
 * Source: https://github.com/ltearno/angular2-gwt/
 */
@JsFunction
@FunctionalInterface
public interface JsReduce<T, T2>
{
    T2 execute(T2 accumulator, T value);
}