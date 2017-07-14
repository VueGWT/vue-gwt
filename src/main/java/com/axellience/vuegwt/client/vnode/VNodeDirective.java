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
public final class VNodeDirective extends JsObject
{
    @JsProperty protected String name;
    @JsProperty protected Object value;
    @JsProperty protected Object oldValue;
    @JsProperty protected Object expression;
    @JsProperty protected String arg;
    @JsProperty protected JsObject modifiers;

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
    public final JsObject getModifiers()
    {
        return modifiers;
    }

    @JsOverlay
    public final VNodeDirective setModifiers(JsObject modifiers)
    {
        this.modifiers = modifiers;
        return this;
    }

    @JsOverlay
    public final VNodeDirective modifier(String name, boolean modifier)
    {
        if (this.modifiers == null)
            this.modifiers = new JsObject();

        this.modifiers.set(name, modifier);
        return this;
    }
}
