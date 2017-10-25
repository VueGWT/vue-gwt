package com.axellience.vuegwt.client.component;

import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.client.component.options.functions.OnEvent;
import com.axellience.vuegwt.client.component.options.functions.OnNextTick;
import com.axellience.vuegwt.client.component.options.watch.ChangeTrigger;
import com.axellience.vuegwt.client.component.options.watch.OnValueChange;
import com.axellience.vuegwt.client.component.options.watch.WatcherRegistration;
import com.axellience.vuegwt.client.vnode.ScopedSlot;
import com.axellience.vuegwt.client.vnode.VNode;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import elemental2.core.Array;
import elemental2.dom.Element;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.JsPropertyMap;

/**
 * The Java representation of a Vue Component.
 * Whenever you want to add a component to your application you should extends this class and add
 * the {@link Component} annotation.
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public abstract class VueComponent
{
    /* ---------------------------------------------

              Instance Properties and Methods

      ---------------------------------------------*/

    @JsProperty private JsPropertyMap $data;
    @JsProperty private Element $el;
    @JsProperty private VueComponentOptions $options;
    @JsProperty private VueComponent $parent;
    @JsProperty private VueComponent $root;
    @JsProperty private Array<VueComponent> $children;
    @JsProperty private Object $refs;
    @JsProperty private JsPropertyMap<Array<VNode>> $slots;
    @JsProperty private JsPropertyMap<ScopedSlot> $scopedSlots;
    @JsProperty private boolean $isServer;
    @JsProperty private Object $ssrContext;
    @JsProperty private Object $props;
    @JsProperty private Object $vnode;
    @JsProperty private JsPropertyMap<String> $attrs;
    @JsProperty private Object $listeners;

    @JsProperty public String _uid;

    // @formatter:off
    // Data
    public native <T> WatcherRegistration $watch(String toWatch, OnValueChange<T> onValueChange);
    public native <T> WatcherRegistration $watch(ChangeTrigger<T> changeTrigger,
        OnValueChange<T> onValueChange);

    public native <T> T $set(Object object, String key, T value);
    public native boolean $set(Object object, String key, boolean value);
    public native byte $set(Object object, String key, byte value);
    public native char $set(Object object, String key, char value);
    public native float $set(Object object, String key, float value);
    public native int $set(Object object, String key, int value);
    public native short $set(Object object, String key, short value);
    public native double $set(Object object, String key, double value);
    public native Object $delete(Object object, String key);

    // Events
    public native void $on(String name, OnEvent callback);
    public native void $once(String name, OnEvent callback);
    public native void $off(String name, OnEvent callback);
    public native void $emit(String name, Object... param);

    // Lifecycle
    public native VueComponent $mount();
    public native VueComponent $mount(Element element);
    public native VueComponent $mount(String element);
    public native VueComponent $mount(Element element, boolean hydrating);
    public native VueComponent $mount(String element, boolean hydrating);
    public native void $forceUpdate();
    public native void $nextTick(OnNextTick onNextTick);
    public native void $destroy();
    // @formatter:on

    @JsOverlay
    public final JsPropertyMap $data()
    {
        return $data;
    }

    @JsOverlay
    public final Element $el()
    {
        return $el;
    }

    @JsOverlay
    public final VueComponentOptions $options()
    {
        return $options;
    }

    @JsOverlay
    public final VueComponent $parent()
    {
        return $parent;
    }

    @JsOverlay
    public final VueComponent $root()
    {
        return $root;
    }

    @JsOverlay
    public final Array<VueComponent> $children()
    {
        return $children;
    }

    @JsOverlay
    public final Object $refs()
    {
        return $refs;
    }

    @JsOverlay
    public final JsPropertyMap<Array<VNode>> $slots()
    {
        return $slots;
    }

    @JsOverlay
    public final JsPropertyMap<ScopedSlot> $scopedSlots()
    {
        return $scopedSlots;
    }

    @JsOverlay
    public final boolean $isServer()
    {
        return $isServer;
    }

    @JsOverlay
    public final Object $ssrContext()
    {
        return $ssrContext;
    }

    @JsOverlay
    public final Object $props()
    {
        return $props;
    }

    @JsOverlay
    public final Object $vnode()
    {
        return $vnode;
    }

    @JsOverlay
    public final JsPropertyMap<String> $attrs()
    {
        return $attrs;
    }

    @JsOverlay
    public final Object $listeners()
    {
        return $listeners;
    }
}