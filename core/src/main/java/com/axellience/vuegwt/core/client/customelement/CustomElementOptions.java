package com.axellience.vuegwt.core.client.customelement;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.jsnative.jsfunctions.JsConsumer;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class CustomElementOptions<T extends IsVueComponent>
{
    @JsProperty private JsConsumer<VueCustomElement<T>> constructorCallback;
    @JsProperty private JsConsumer<VueCustomElement<T>> connectedCallback;
    @JsProperty private JsConsumer<VueCustomElement<T>> disconnectedCallback;
    @JsProperty private AttributeChangedCallback<T> attributeChangedCallback;
    @JsProperty private int destroyTimeout;
    @JsProperty private boolean shadow;
    @JsProperty private String shadowCss;

    @JsOverlay
    public final JsConsumer<VueCustomElement<T>> getConstructorCallback()
    {
        return constructorCallback;
    }

    @JsOverlay
    public final CustomElementOptions<T> setConstructorCallback(
        JsConsumer<VueCustomElement<T>> constructorCallback)
    {
        this.constructorCallback = constructorCallback;
        return this;
    }

    @JsOverlay
    public final JsConsumer<VueCustomElement<T>> getConnectedCallback()
    {
        return connectedCallback;
    }

    @JsOverlay
    public final CustomElementOptions<T> setConnectedCallback(
        JsConsumer<VueCustomElement<T>> connectedCallback)
    {
        this.connectedCallback = connectedCallback;
        return this;
    }

    @JsOverlay
    public final JsConsumer<VueCustomElement<T>> getDisconnectedCallback()
    {
        return disconnectedCallback;
    }

    @JsOverlay
    public final CustomElementOptions<T> setDisconnectedCallback(
        JsConsumer<VueCustomElement<T>> disconnectedCallback)
    {
        this.disconnectedCallback = disconnectedCallback;
        return this;
    }

    @JsOverlay
    public final AttributeChangedCallback<T> getAttributeChangedCallback()
    {
        return attributeChangedCallback;
    }

    @JsOverlay
    public final CustomElementOptions<T> setAttributeChangedCallback(
        AttributeChangedCallback<T> attributeChangedCallback)
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
    public final CustomElementOptions<T> setDestroyTimeout(int destroyTimeout)
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
    public final CustomElementOptions<T> setShadow(boolean shadow)
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
    public final CustomElementOptions<T> setShadowCss(String shadowCss)
    {
        this.shadowCss = shadowCss;
        return this;
    }
}
