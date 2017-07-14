package com.axellience.vuegwt.client.component.options.watch;

import jsinterop.annotations.JsFunction;

/**
 * Callback for change on watched value
 * @author Adrien Baron
 */
@JsFunction
@FunctionalInterface
public interface ChangeTrigger
{
    Object watchedValue();
}