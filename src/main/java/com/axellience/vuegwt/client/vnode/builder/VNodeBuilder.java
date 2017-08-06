package com.axellience.vuegwt.client.vnode.builder;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.vnode.VNode;
import com.axellience.vuegwt.client.vnode.VNodeData;
import com.axellience.vuegwt.client.vue.VueJsConstructor;

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
     * @param vueJsConstructor Constructor for the Component we want
     * @param children Children
     * @return a new VNode of this Component
     */
    public VNode el(VueJsConstructor<VueComponent> vueJsConstructor, Object... children)
    {
        return this.function.create(vueJsConstructor, children, null);
    }

    /**
     * Create a VNode with the given Component
     * @param vueComponentClass Class for the {@link VueComponent} we want
     * @param data Information for the new VNode (attributes...)
     * @param children Children
     * @return a new VNode of this Component
     */
    public VNode el(Class<VueComponent> vueComponentClass, VNodeData data, Object... children)
    {
        return this.function.create(vueComponentClass, data, children);
    }
}
