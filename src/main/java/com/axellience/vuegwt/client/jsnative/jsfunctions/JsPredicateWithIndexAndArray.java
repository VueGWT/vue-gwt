package com.axellience.vuegwt.client.jsnative.jsfunctions;

import com.axellience.vuegwt.client.jsnative.types.JsArray;
import jsinterop.annotations.JsFunction;

/**
 * @author Adrien Baron
 */
@JsFunction
@FunctionalInterface
public interface JsPredicateWithIndexAndArray<T>
{
    boolean execute(T a, int index, JsArray<T> array);
}
