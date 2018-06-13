package com.axellience.vuegwt.core.client.observer.functions;

import elemental2.core.JsArray;
import jsinterop.annotations.JsFunction;

@JsFunction
@FunctionalInterface
public interface VueObserveArray {

  <T> void observeArray(JsArray<T> objects);
}
