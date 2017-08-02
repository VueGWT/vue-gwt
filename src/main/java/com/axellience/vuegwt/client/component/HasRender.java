package com.axellience.vuegwt.client.component;

import com.axellience.vuegwt.client.vnode.VNode;
import com.axellience.vuegwt.client.vnode.builder.VNodeBuilder;

/**
 * @author Adrien Baron
 */
public interface HasRender
{
    VNode render(VNodeBuilder builder);
}
