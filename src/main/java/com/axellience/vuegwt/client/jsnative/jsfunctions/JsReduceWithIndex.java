package com.axellience.vuegwt.client.jsnative.jsfunctions;

import jsinterop.annotations.JsFunction;

/**
 * Source: https://github.com/ltearno/angular2-gwt/
 */
@JsFunction
@FunctionalInterface
public interface JsReduceWithIndex<T, T2>
{
    T2 execute(T2 accumulator, T value, int currentIndex);
}