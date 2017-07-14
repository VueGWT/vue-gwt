package com.axellience.vuegwt.client.jsnative.jstypes.arrayfunctions;

import com.axellience.vuegwt.client.jsnative.jstypes.JsArray;
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
