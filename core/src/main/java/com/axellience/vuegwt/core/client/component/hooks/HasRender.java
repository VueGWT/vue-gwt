package com.axellience.vuegwt.core.client.component.hooks;

import com.axellience.vuegwt.core.annotations.component.HookMethod;

import com.axellience.vuegwt.core.client.vnode.VNode;
import com.axellience.vuegwt.core.client.vnode.builder.VNodeBuilder;
import jsinterop.annotations.JsMethod;

/**
 * @author Adrien Baron
 */
public interface HasRender
{
    @HookMethod
    @JsMethod
    VNode render(VNodeBuilder builder);
}
