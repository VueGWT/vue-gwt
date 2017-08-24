package com.axellience.vuegwt.client.component.options.watch;

import jsinterop.annotations.JsFunction;

/**
 * Callback when a Vue.js event is fired by a child component
 */
@JsFunction
@FunctionalInterface
public interface OnValueChange<T>
{
    void exec(T newValue, T oldValue);
}