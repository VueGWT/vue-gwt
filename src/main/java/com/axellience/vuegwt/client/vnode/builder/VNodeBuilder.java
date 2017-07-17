package com.axellience.vuegwt.client.vnode.builder;

import com.axellience.vuegwt.client.Vue;
import com.axellience.vuegwt.client.vnode.VNode;
import com.axellience.vuegwt.client.vnode.VNodeData;
import com.axellience.vuegwt.client.vue.VueConstructor;

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

    public VNode el(VueConstructor<Vue> vueConstructor, Object... children)
    {
        return this.function.create(vueConstructor,
            children,
            null);
    }

    public VNode el(Class<Vue> vueConstructor, VNodeData data, Object... children)
    {
        return this.function.create(vueConstructor,
            data,
            children);
    }
}
