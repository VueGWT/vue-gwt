package com.axellience.vuegwt.client.vnode;

import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public final class VNodeComponentOptions extends JsObject
{
    @JsProperty protected Object propsData;
    @JsProperty protected Object listeners;
    @JsProperty protected Object children;
    @JsProperty protected String tag;

    @JsOverlay
    public final Object getPropsData()
    {
        return propsData;
    }

    @JsOverlay
    public final VNodeComponentOptions setPropsData(Object propsData)
    {
        this.propsData = propsData;
        return this;
    }

    @JsOverlay
    public final Object getListeners()
    {
        return listeners;
    }

    @JsOverlay
    public final VNodeComponentOptions setListeners(Object listeners)
    {
        this.listeners = listeners;
        return this;
    }

    @JsOverlay
    public final Object getChildren()
    {
        return children;
    }

    @JsOverlay
    public final VNodeComponentOptions setChildren(Object children)
    {
        this.children = children;
        return this;
    }

    @JsOverlay
    public final String getTag()
    {
        return tag;
    }

    @JsOverlay
    public final VNodeComponentOptions setTag(String tag)
    {
        this.tag = tag;
        return this;
    }
}
