package com.axellience.vuegwt.client.vnode;

import com.axellience.vuegwt.client.Vue;
import com.axellience.vuegwt.client.jsnative.jstypes.JsArray;
import com.google.gwt.dom.client.Element;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public final class VNode
{
    @JsProperty protected String tag;
    @JsProperty protected VNodeData data;
    @JsProperty protected JsArray<VNode> children;
    @JsProperty protected String text;
    @JsProperty protected Element elm;
    @JsProperty protected String ns;
    @JsProperty protected Vue context;
    @JsProperty protected Object key;
    @JsProperty protected VNodeComponentOptions componentOptions;
    @JsProperty protected Vue componentInstance;
    @JsProperty protected VNode parent;
    @JsProperty protected boolean raw;
    @JsProperty protected boolean isStatic;
    @JsProperty protected boolean isRootInsert;
    @JsProperty protected boolean isComment;

    @JsOverlay
    public final String getTag()
    {
        return tag;
    }

    @JsOverlay
    public final VNode setTag(String tag)
    {
        this.tag = tag;
        return this;
    }

    @JsOverlay
    public final VNodeData getData()
    {
        return data;
    }

    @JsOverlay
    public final VNode setData(VNodeData data)
    {
        this.data = data;
        return this;
    }

    @JsOverlay
    public final JsArray<VNode> getChildren()
    {
        return children;
    }

    @JsOverlay
    public final VNode setChildren(JsArray<VNode> children)
    {
        this.children = children;
        return this;
    }

    @JsOverlay
    public final VNode addChild(VNode child)
    {
        if (this.children == null)
            this.children = new JsArray<>();

        this.children.push(child);
        return this;
    }

    @JsOverlay
    public final String getText()
    {
        return text;
    }

    @JsOverlay
    public final VNode setText(String text)
    {
        this.text = text;
        return this;
    }

    @JsOverlay
    public final Element getElm()
    {
        return elm;
    }

    @JsOverlay
    public final VNode setElm(Element elm)
    {
        this.elm = elm;
        return this;
    }

    @JsOverlay
    public final String getNs()
    {
        return ns;
    }

    @JsOverlay
    public final VNode setNs(String ns)
    {
        this.ns = ns;
        return this;
    }

    @JsOverlay
    public final Vue getContext()
    {
        return context;
    }

    @JsOverlay
    public final VNode setContext(Vue context)
    {
        this.context = context;
        return this;
    }

    @JsOverlay
    public final Object getKey()
    {
        return key;
    }

    @JsOverlay
    public final VNode setKey(Object key)
    {
        this.key = key;
        return this;
    }

    @JsOverlay
    public final VNodeComponentOptions getComponentOptions()
    {
        return componentOptions;
    }

    @JsOverlay
    public final VNode setComponentOptions(VNodeComponentOptions componentOptions)
    {
        this.componentOptions = componentOptions;
        return this;
    }

    @JsOverlay
    public final Vue getComponentInstance()
    {
        return componentInstance;
    }

    @JsOverlay
    public final VNode setComponentInstance(Vue componentInstance)
    {
        this.componentInstance = componentInstance;
        return this;
    }

    @JsOverlay
    public final VNode getParent()
    {
        return parent;
    }

    @JsOverlay
    public final VNode setParent(VNode parent)
    {
        this.parent = parent;
        return this;
    }

    @JsOverlay
    public final boolean isRaw()
    {
        return raw;
    }

    @JsOverlay
    public final VNode setRaw(boolean raw)
    {
        this.raw = raw;
        return this;
    }

    @JsOverlay
    public final boolean isStatic()
    {
        return isStatic;
    }

    @JsOverlay
    public final VNode setStatic(boolean aStatic)
    {
        isStatic = aStatic;
        return this;
    }

    @JsOverlay
    public final boolean isRootInsert()
    {
        return isRootInsert;
    }

    @JsOverlay
    public final VNode setRootInsert(boolean rootInsert)
    {
        isRootInsert = rootInsert;
        return this;
    }

    @JsOverlay
    public final boolean isComment()
    {
        return isComment;
    }

    @JsOverlay
    public final VNode setComment(boolean comment)
    {
        isComment = comment;
        return this;
    }
}
