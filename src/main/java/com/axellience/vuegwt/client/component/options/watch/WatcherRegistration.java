package com.axellience.vuegwt.client.component.options.watch;

import jsinterop.annotations.JsFunction;

/**
 * Unregister function sent by Vue to unregister a Watcher
 * @author Adrien Baron
 */
@JsFunction
@FunctionalInterface
public interface WatcherRegistration
{
    void unregister();
}