package com.axellience.vuegwt.client.component;

import com.axellience.vuegwt.client.vnode.VNode;
import com.axellience.vuegwt.client.vnode.builder.VNodeBuilder;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType(isNative = true)
public interface HasRender
{
    VNode render(VNodeBuilder builder);
}
