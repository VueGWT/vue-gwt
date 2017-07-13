package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.functions.ChangeTrigger;
import com.axellience.vuegwt.client.functions.OnEvent;
import com.axellience.vuegwt.client.functions.OnNextTick;
import com.axellience.vuegwt.client.functions.OnValueChange;
import com.axellience.vuegwt.client.functions.WatcherRegistration;
import com.axellience.vuegwt.client.jsnative.VueGwtToolsInjector;
import com.axellience.vuegwt.client.jsnative.types.JsArray;
import com.axellience.vuegwt.client.jsnative.types.JsObject;
import com.axellience.vuegwt.client.options.VueComponentOptions;
import com.google.gwt.dom.client.Element;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

/**
 * The Java representation of a Vue Component.
 * Whenever you want to add a component to your application you should extends this class and add
 * the
 * {@link com.axellience.vuegwt.jsr69.component.annotations.Component} annotation.
 * @author Adrien Baron
 */
public abstract class VueComponent
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
    @JsProperty public JsObject $slots;
    @JsProperty public JsObject $scopedSlots;
    @JsProperty public boolean $isServer;
    @JsProperty public Object $ssrContext;
    @JsProperty public Object $props;
    @JsProperty public Object $vnode;
    @JsProperty public JsObject $attrs;
    @JsProperty public Object $listeners;

    @JsProperty public String _uid;

    /**
     * Used to customize {@link VueComponentOptions} for this {@link VueComponent}.
     * <p>
     * This is called once when creating the {@link VueComponentOptions} of our Component.
     * This {@link VueComponentOptions} will be passed to Vue when registering your Component.
     * This can be used to set custom properties for plugins like Vue Router.
     * @param componentOptions The options that we can customize in the method
     */
    public void customizeOptions(VueComponentOptions componentOptions)
    {

    }

    /**
     * Lifecycle hooks
     * By default they are not copied, they are here to facilitate development
     */
    @JsMethod
    protected void beforeCreate()
    {

    }

    @JsMethod
    protected abstract void created();

    @JsMethod
    protected void beforeMount()
    {

    }

    @JsMethod
    protected void mounted()
    {

    }

    @JsMethod
    protected void beforeUpdate()
    {

    }

    @JsMethod
    protected void updated()
    {

    }

    @JsMethod
    protected void activated()
    {

    }

    @JsMethod
    protected void deactivated()
    {

    }

    @JsMethod
    protected void beforeDestroy()
    {

    }

    @JsMethod
    protected void destroyed()
    {

    }

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