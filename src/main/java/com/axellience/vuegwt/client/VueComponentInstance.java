package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.functions.ChangeTrigger;
import com.axellience.vuegwt.client.functions.OnEvent;
import com.axellience.vuegwt.client.functions.OnNextTick;
import com.axellience.vuegwt.client.functions.OnValueChange;
import com.axellience.vuegwt.client.functions.WatcherRegistration;
import com.axellience.vuegwt.client.jsnative.JsArray;
import com.axellience.vuegwt.client.jsnative.JsObject;
import com.google.gwt.dom.client.Element;
import jsinterop.annotations.JsType;

/**
 * Represent an instantiated VueComponent
 */
@JsType
public class VueComponentInstance
{
    public JsObject                      $data;
    public Element                       $el;
    public JsObject                      $options;
    public VueComponentInstance          $parent;
    public VueComponentInstance          $root;
    public JsArray<VueComponentInstance> $children;
    public JsObject                      $slots;
    public JsObject                      $scopedSlots;
    public JsObject                      $refs;
    public boolean                       $isServer;

    // Data
    public native WatcherRegistration $watch(String toWatch, OnValueChange onValueChange);
    public native WatcherRegistration $watch(ChangeTrigger changeTrigger, OnValueChange onValueChange);

    public native Object $set(Object object, String key, Object value);

    public native Object $set(Object object, String key, boolean value);

    public native Object $set(Object object, String key, byte value);

    public native Object $set(Object object, String key, char value);

    public native Object $set(Object object, String key, float value);

    public native Object $set(Object object, String key, int value);

    public native Object $set(Object object, String key, short value);

    public native Object $set(Object object, String key, double value);

    public native Object $delete(Object object, String key);

    // Events
    public native void $on(String name, OnEvent callback);

    public native void $once(String name, OnEvent callback);

    public native void $off(String name, OnEvent callback);

    public native void $emit(String name, Object... param);

    // Lifecycle
    public native VueComponentInstance $mount();

    public native VueComponentInstance $mount(Element element);

    public native VueComponentInstance $mount(String element);

    public native VueComponentInstance $mount(Element element, boolean hydrating);

    public native VueComponentInstance $mount(String element, boolean hydrating);

    public native void $forceUpdate();

    public native void $nextTick(OnNextTick onNextTick);

    public native void $destroy();
}