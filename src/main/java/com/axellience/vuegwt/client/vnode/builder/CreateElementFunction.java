package com.axellience.vuegwt.client.vnode.builder;

import com.axellience.vuegwt.client.vnode.VNode;
import jsinterop.annotations.JsFunction;

/**
 * @author Adrien Baron
 */
@JsFunction
@FunctionalInterface
public interface CreateElementFunction
{
    VNode create(Object tag, Object dataOrChildren, Object children);
}
