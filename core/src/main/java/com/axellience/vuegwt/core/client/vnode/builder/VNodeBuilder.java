package com.axellience.vuegwt.core.client.vnode.builder;

import com.axellience.vuegwt.core.client.VueGWT;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.vnode.VNode;
import com.axellience.vuegwt.core.client.vnode.VNodeData;
import com.axellience.vuegwt.core.client.vue.VueComponentFactory;
import com.axellience.vuegwt.core.client.vue.VueJsConstructor;

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
     * Create a VNode with the given {@link IsVueComponent}
     * @param isVueComponentClass Class for the {@link IsVueComponent} we want
     * @param children Children
     * @param <T> The type of the {@link IsVueComponent}
     * @return a new VNode of this Component
     */
    public <T extends IsVueComponent> VNode el(Class<T> isVueComponentClass, Object... children)
    {
        return el(VueGWT.getJsConstructor(isVueComponentClass), children);
    }

    /**
     * Create a VNode with the given {@link IsVueComponent}
     * @param isVueComponentClass Class for the {@link IsVueComponent} we want
     * @param data Information for the new VNode (attributes...)
     * @param children Children
     * @param <T> The type of the {@link IsVueComponent}
     * @return a new VNode of this Component
     */
    public <T extends IsVueComponent> VNode el(Class<T> isVueComponentClass, VNodeData data, Object... children)
    {
        return el(VueGWT.getJsConstructor(isVueComponentClass), data, children);
    }

    /**
     * Create a VNode with the {@link IsVueComponent} of the given {@link VueComponentFactory}
     * @param vueFactory {@link VueComponentFactory} for the Component we want
     * @param children Children
     * @param <T> The type of the {@link IsVueComponent}
     * @return a new VNode of this Component
     */
    public <T extends IsVueComponent> VNode el(VueComponentFactory<T> vueFactory, Object... children)
    {
        return el(vueFactory.getJsConstructor(), children, null);
    }

    /**
     * Create a VNode with the {@link IsVueComponent} of the given {@link VueComponentFactory}
     * @param vueFactory {@link VueComponentFactory} for the Component we want
     * @param data Information for the new VNode (attributes...)
     * @param children Children
     * @param <T> The type of the {@link IsVueComponent}
     * @return a new VNode of this Component
     */
    public <T extends IsVueComponent> VNode el(VueComponentFactory<T> vueFactory, VNodeData data, Object... children)
    {
        return el(vueFactory.getJsConstructor(), data, children);
    }

    /**
     * Create a VNode with the {@link IsVueComponent} of the given {@link VueJsConstructor}
     * @param vueJsConstructor {@link VueJsConstructor} for the Component we want
     * @param children Children
     * @param <T> The type of the {@link IsVueComponent}
     * @return a new VNode of this Component
     */
    public <T extends IsVueComponent> VNode el(VueJsConstructor<T> vueJsConstructor, Object... children)
    {
        return el(vueJsConstructor, null, children);
    }

    /**
     * Create a VNode with the {@link IsVueComponent} of the given {@link VueJsConstructor}
     * @param vueJsConstructor {@link VueJsConstructor} for the Component we want
     * @param data Information for the new VNode (attributes...)
     * @param children Children
     * @param <T> The type of the {@link IsVueComponent}
     * @return a new VNode of this Component
     */
    public <T extends IsVueComponent> VNode el(VueJsConstructor<T> vueJsConstructor, VNodeData data, Object... children)
    {
        return this.function.create(vueJsConstructor, data, children);
    }
}
