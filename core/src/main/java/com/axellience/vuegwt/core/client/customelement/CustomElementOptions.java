package com.axellience.vuegwt.core.client.customelement;

import com.axellience.vuegwt.core.client.jsnative.jsfunctions.JsRunnable;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class CustomElementOptions
{
    @JsProperty private JsRunnable constructorCallback;
    @JsProperty private JsRunnable connectedCallback;
    @JsProperty private JsRunnable disconnectedCallback;
    @JsProperty private AttributeChangedCallback attributeChangedCallback;
    @JsProperty private int destroyTimeout;
    @JsProperty private boolean shadow;
    @JsProperty private String shadowCss;

    @JsOverlay
    public final JsRunnable getConstructorCallback()
    {
        return constructorCallback;
    }

    @JsOverlay
    public final CustomElementOptions setConstructorCallback(JsRunnable constructorCallback)
    {
        this.constructorCallback = constructorCallback;
        return this;
    }

    @JsOverlay
    public final JsRunnable getConnectedCallback()
    {
        return connectedCallback;
    }

    @JsOverlay
    public final CustomElementOptions setConnectedCallback(JsRunnable connectedCallback)
    {
        this.connectedCallback = connectedCallback;
        return this;
    }

    @JsOverlay
    public final JsRunnable getDisconnectedCallback()
    {
        return disconnectedCallback;
    }

    @JsOverlay
    public final CustomElementOptions setDisconnectedCallback(JsRunnable disconnectedCallback)
    {
        this.disconnectedCallback = disconnectedCallback;
        return this;
    }

    @JsOverlay
    public final AttributeChangedCallback getAttributeChangedCallback()
    {
        return attributeChangedCallback;
    }

    @JsOverlay
    public final CustomElementOptions setAttributeChangedCallback(
        AttributeChangedCallback attributeChangedCallback)
    {
        this.attributeChangedCallback = attributeChangedCallback;
        return this;
    }

    @JsOverlay
    public final int getDestroyTimeout()
    {
        return destroyTimeout;
    }

    @JsOverlay
    public final CustomElementOptions setDestroyTimeout(int destroyTimeout)
    {
        this.destroyTimeout = destroyTimeout;
        return this;
    }

    @JsOverlay
    public final boolean isShadow()
    {
        return shadow;
    }

    @JsOverlay
    public final CustomElementOptions setShadow(boolean shadow)
    {
        this.shadow = shadow;
        return this;
    }

    @JsOverlay
    public final String getShadowCss()
    {
        return shadowCss;
    }

    @JsOverlay
    public final CustomElementOptions setShadowCss(String shadowCss)
    {
        this.shadowCss = shadowCss;
        return this;
    }
}
