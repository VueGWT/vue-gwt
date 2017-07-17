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

    /**
     * Create an empty VNode
     * @return a new empty VNode
     */
    public VNode el()
    {
        return this.function.create(null, null, null);
    }

    /**
     * Create a VNode with the given HTML tag
     * @param tag HTML tag for the new VNode
     * @param children Children
     * @return a new VNode of this tag
     */
    public VNode el(String tag, Object... children)
    {
        return this.function.create(tag, children, null);
    }

    /**
     * Create a VNode with the given HTML tag
     * @param tag HTML tag for the new VNode
     * @param data Information for the new VNode (attributes...)
     * @param children Children
     * @return a new VNode of this tag
     */
    public VNode el(String tag, VNodeData data, Object... children)
    {
        return this.function.create(tag, data, children);
    }

    /**
     * Create a VNode with the given Component
     * @param vueConstructor Constructor for the Component we want
     * @param children Children
     * @return a new VNode of this Component
     */
    public VNode el(VueConstructor<Vue> vueConstructor, Object... children)
    {
        return this.function.create(vueConstructor, children, null);
    }

    /**
     * Create a VNode with the given Component
     * @param vueConstructor Constructor for the Component we want
     * @param data Information for the new VNode (attributes...)
     * @param children Children
     * @return a new VNode of this Component
     */
    public VNode el(Class<Vue> vueConstructor, VNodeData data, Object... children)
    {
        return this.function.create(vueConstructor, data, children);
    }
}
