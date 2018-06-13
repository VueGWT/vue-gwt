package com.axellience.vuegwt.core.client.component.options.watch;

import jsinterop.annotations.JsFunction;

/**
 * Callback for change on watched value
 *
 * @author Adrien Baron
 */
@JsFunction
@FunctionalInterface
public interface ChangeTrigger<T> {

  T watchedValue();
}