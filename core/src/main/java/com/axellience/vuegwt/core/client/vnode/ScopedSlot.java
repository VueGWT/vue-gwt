package com.axellience.vuegwt.core.client.vnode;

import jsinterop.annotations.JsFunction;

/**
 * @author Adrien Baron
 */
@JsFunction
@FunctionalInterface
public interface ScopedSlot {

  Object execute(Object props);
}
