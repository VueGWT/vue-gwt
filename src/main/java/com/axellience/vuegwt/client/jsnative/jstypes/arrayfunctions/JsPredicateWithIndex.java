package com.axellience.vuegwt.client.jsnative.jstypes.arrayfunctions;

import jsinterop.annotations.JsFunction;

/**
 * @author Adrien Baron
 */
@JsFunction
@FunctionalInterface
public interface JsPredicateWithIndex<T>
{
    boolean execute(T a, int index);
}
