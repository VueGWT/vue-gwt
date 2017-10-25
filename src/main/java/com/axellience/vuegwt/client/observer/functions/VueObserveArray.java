package com.axellience.vuegwt.client.observer.functions;

import elemental2.core.Array;
import jsinterop.annotations.JsFunction;

@JsFunction
@FunctionalInterface
public interface VueObserveArray
{
    <T> void observeArray(Array<T> objects);
}
