package com.axellience.vuegwt.core.client.vnode;

import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.JsPropertyMap;

/**
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public final class VNodeDirective
{
    @JsProperty protected String name;
    @JsProperty protected Object value;
    @JsProperty protected Object oldValue;
    @JsProperty protected Object expression;
    @JsProperty protected String arg;
    @JsProperty protected JsPropertyMap modifiers;

    @JsOverlay
    public final String getName()
    {
        return name;
    }

    @JsOverlay
    public final VNodeDirective setName(String name)
    {
        this.name = name;
        return this;
    }

    @JsOverlay
    public final Object getValue()
    {
        return value;
    }

    @JsOverlay
    public final VNodeDirective setValue(Object value)
    {
        this.value = value;
        return this;
    }

    @JsOverlay
    public final Object getOldValue()
    {
        return oldValue;
    }

    @JsOverlay
    public final VNodeDirective setOldValue(Object oldValue)
    {
        this.oldValue = oldValue;
        return this;
    }

    @JsOverlay
    public final Object getExpression()
    {
        return expression;
    }

    @JsOverlay
    public final VNodeDirective setExpression(Object expression)
    {
        this.expression = expression;
        return this;
    }

    @JsOverlay
    public final String getArg()
    {
        return arg;
    }

    @JsOverlay
    public final VNodeDirective setArg(String arg)
    {
        this.arg = arg;
        return this;
    }

    @JsOverlay
    public final JsPropertyMap getModifiers()
    {
        return modifiers;
    }

    @JsOverlay
    public final VNodeDirective setModifiers(JsPropertyMap modifiers)
    {
        this.modifiers = modifiers;
        return this;
    }

    @JsOverlay
    public final VNodeDirective modifier(String name, boolean modifier)
    {
        if (this.modifiers == null)
            this.modifiers = JsPropertyMap.of();

        this.modifiers.set(name, modifier);
        return this;
    }
}
