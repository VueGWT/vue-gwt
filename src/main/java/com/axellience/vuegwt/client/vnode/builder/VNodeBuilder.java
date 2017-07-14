package com.axellience.vuegwt.client.vnode.builder;

import com.axellience.vuegwt.client.VueOptionsCache;
import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.vnode.VNode;
import com.axellience.vuegwt.client.vnode.VNodeData;

/**
 * @author Adrien Baron
 */
public class VNodeBuilder
{
    private final CreateElementFunction function;

    public VNodeBuilder(CreateElementFunction function)
    {
        this.function = function;
    }

    public VNode el()
    {
        return this.function.create(null, null, null);
    }

    public VNode el(String tag, Object... children)
    {
        return this.function.create(tag, children, null);
    }

    public VNode el(String tag, VNodeData data, Object... children)
    {
        return this.function.create(tag, data, children);
    }

    public VNode el(Class<VueComponent> componentClass, Object... children)
    {
        return this.function.create(VueOptionsCache.getComponentOptions(componentClass),
            children,
            null);
    }

    public VNode el(Class<VueComponent> componentClass, VNodeData data, Object... children)
    {
        return this.function.create(VueOptionsCache.getComponentOptions(componentClass),
            data,
            children);
    }
}
