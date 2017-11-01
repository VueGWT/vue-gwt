package com.axellience.vuegwt.core.client.vue;

import com.axellience.vuegwt.core.client.jsnative.jsfunctions.JsConsumer;
import jsinterop.annotations.JsFunction;

/**
 * Used to asynchronously provide the local {@link VueJsConstructor} when creating a {@link
 * VueJsConstructor}.
 * Using this async mechanism allows us to have cylces in component definitions (which is supported
 * by Vue.js) even when injecting the dependencies with Gin.
 * @author Adrien Baron
 */
@JsFunction
@FunctionalInterface
public interface VueJsAsyncProvider<T>
{
    void run(JsConsumer<T> resolve);
}
