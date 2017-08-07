package com.axellience.vuegwt.client.vue;

import com.axellience.vuegwt.client.jsnative.jsfunctions.JsConsumer;
import jsinterop.annotations.JsFunction;

/**
 * @author Adrien Baron
 */
@JsFunction
@FunctionalInterface
public interface VueJsAsyncProvider<T>
{
    void run(JsConsumer<T> resolve);
}
