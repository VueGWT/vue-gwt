package com.axellience.vuegwt.client.functions;

import jsinterop.annotations.JsFunction;

@JsFunction
@FunctionalInterface
public interface WatcherRegistration
{
    void unregister();
}
