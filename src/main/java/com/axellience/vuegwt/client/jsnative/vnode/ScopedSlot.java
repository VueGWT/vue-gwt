package com.axellience.vuegwt.client.jsnative.vnode;

import jsinterop.annotations.JsFunction;

/**
 * @author Adrien Baron
 */
@JsFunction
@FunctionalInterface
public interface ScopedSlot
{
    Object execute(Object props);
}
