package com.axellience.vuegwt.client.observer.functions;

import jsinterop.annotations.JsFunction;

@JsFunction
@FunctionalInterface
public interface VueWalk
{
    void walk(Object toMakeReactive);
}
