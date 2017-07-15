package com.axellience.vuegwt.client.component;

import com.axellience.vuegwt.client.component.hooks.HasCreated;
import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.client.component.options.functions.OnEvent;
import com.axellience.vuegwt.client.component.options.functions.OnNextTick;
import com.axellience.vuegwt.client.component.options.watch.ChangeTrigger;
import com.axellience.vuegwt.client.component.options.watch.OnValueChange;
import com.axellience.vuegwt.client.component.options.watch.WatcherRegistration;
import com.axellience.vuegwt.client.jsnative.jstypes.JsArray;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.tools.VueGwtToolsInjector;
import com.axellience.vuegwt.client.vnode.ScopedSlot;
import com.axellience.vuegwt.client.vnode.VNode;
import com.google.gwt.dom.client.Element;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * The Java representation of a Vue Component.
 * Whenever you want to add a component to your application you should extends this class and add
 * the
 * {@link com.axellience.vuegwt.jsr69.component.annotations.Component} annotation.
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public abstract class VueComponent implements HasCreated
{
    static
    {
        // Inject the JS tools in the page
        VueGwtToolsInjector.inject();
    }

    @JsProperty public JsObject $data;
    @JsProperty public Element $el;
    @JsProperty public VueComponentOptions $options;
    @JsProperty public VueComponent $parent;
    @JsProperty public VueComponent $root;
    @JsProperty public JsArray<VueComponent> $children;
    @JsProperty public Object $refs;
    @JsProperty public JsObject<VNode> $slots;
    @JsProperty public JsObject<ScopedSlot> $scopedSlots;
    @JsProperty public boolean $isServer;
    @JsProperty public Object $ssrContext;
    @JsProperty public Object $props;
    @JsProperty public Object $vnode;
    @JsProperty public JsObject<String> $attrs;
    @JsProperty public Object $listeners;

    @JsProperty public String _uid;

    // Data
    @JsMethod
    public native WatcherRegistration $watch(String toWatch, OnValueChange onValueChange);

    @JsMethod
    public native WatcherRegistration $watch(ChangeTrigger changeTrigger,
        OnValueChange onValueChange);

    @JsMethod
    public native Object $set(Object object, String key, Object value);

    @JsMethod
    public native Object $set(Object object, String key, boolean value);

    @JsMethod
    public native Object $set(Object object, String key, byte value);

    @JsMethod
    public native Object $set(Object object, String key, char value);

    @JsMethod
    public native Object $set(Object object, String key, float value);

    @JsMethod
    public native Object $set(Object object, String key, int value);

    @JsMethod
    public native Object $set(Object object, String key, short value);

    @JsMethod
    public native Object $set(Object object, String key, double value);

    @JsMethod
    public native Object $delete(Object object, String key);

    // Events
    @JsMethod
    public native void $on(String name, OnEvent callback);

    @JsMethod
    public native void $once(String name, OnEvent callback);

    @JsMethod
    public native void $off(String name, OnEvent callback);

    @JsMethod
    public native void $emit(String name, Object... param);

    // Lifecycle
    @JsMethod
    public native VueComponent $mount();

    @JsMethod
    public native VueComponent $mount(Element element);

    @JsMethod
    public native VueComponent $mount(String element);

    @JsMethod
    public native VueComponent $mount(Element element, boolean hydrating);

    @JsMethod
    public native VueComponent $mount(String element, boolean hydrating);

    @JsMethod
    public native void $forceUpdate();

    @JsMethod
    public native void $nextTick(OnNextTick onNextTick);

    @JsMethod
    public native void $destroy();
}