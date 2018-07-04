package com.axellience.vuegwt.core.client.component;

import com.axellience.vuegwt.core.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.core.client.component.options.functions.OnEvent;
import com.axellience.vuegwt.core.client.component.options.functions.OnNextTick;
import com.axellience.vuegwt.core.client.component.options.watch.ChangeTrigger;
import com.axellience.vuegwt.core.client.component.options.watch.OnValueChange;
import com.axellience.vuegwt.core.client.component.options.watch.WatchOptions;
import com.axellience.vuegwt.core.client.component.options.watch.WatcherRegistration;
import com.axellience.vuegwt.core.client.vnode.ScopedSlot;
import com.axellience.vuegwt.core.client.vnode.VNode;
import elemental2.core.Function;
import elemental2.core.JsArray;
import elemental2.dom.Element;
import javaemul.internal.annotations.DoNotAutobox;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

/**
 * The Java representation of a Vue Component.
 *
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public abstract class VueComponent {
    /* ---------------------------------------------

              Instance Properties and Methods

      ---------------------------------------------*/

  @JsProperty
  private JsPropertyMap $data;
  @JsProperty
  private Element $el;
  @JsProperty
  private VueComponentOptions $options;
  @JsProperty
  private IsVueComponent $parent;
  @JsProperty
  private IsVueComponent $root;
  @JsProperty
  private JsArray<IsVueComponent> $children;
  @JsProperty
  private Object $refs;
  @JsProperty
  private JsPropertyMap<JsArray<VNode>> $slots;
  @JsProperty
  private JsPropertyMap<ScopedSlot> $scopedSlots;
  @JsProperty
  private boolean $isServer;
  @JsProperty
  private Object $ssrContext;
  @JsProperty
  private Object $vnode;
  @JsProperty
  private JsPropertyMap<String> $attrs;
  @JsProperty
  private Object $listeners;

  @JsProperty
  public JsPropertyMap<Object> $props;
  @JsProperty
  public String _uid;

  // @formatter:off
  // Data
  public native <T> WatcherRegistration $watch(ChangeTrigger<T> changeTrigger, OnValueChange<T> onValueChange);

  public native WatcherRegistration $watch(Function changeTrigger, Function onValueChange, WatchOptions watchOptions);

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

  public native void $emit(String name, @DoNotAutobox Object... param);

  // Lifecycle
  public native <T extends IsVueComponent> T $mount();

  public native <T extends IsVueComponent> T $mount(Element element);

  public native <T extends IsVueComponent> T $mount(String element);

  public native <T extends IsVueComponent> T $mount(Element element, boolean hydrating);

  public native <T extends IsVueComponent> T $mount(String element, boolean hydrating);

  public native void $forceUpdate();

  public native void $nextTick(OnNextTick onNextTick);

  public native void $destroy();
  // @formatter:on

  @JsOverlay
  public final JsPropertyMap $data() {
    return $data;
  }

  @JsOverlay
  public final <T extends Element> T $el() {
    return (T) $el;
  }

  @JsOverlay
  public final VueComponentOptions $options() {
    return $options;
  }

  @JsOverlay
  public final <T extends IsVueComponent> T $parent() {
    return (T) $parent;
  }

  @JsOverlay
  public final <T extends IsVueComponent> T $root() {
    return (T) $root;
  }

  @JsOverlay
  public final <T extends IsVueComponent> JsArray<T> $children() {
    return (JsArray<T>) $children;
  }

  @JsOverlay
  public final Object $refs() {
    return $refs;
  }

  @JsOverlay
  public final <T> T $ref(String refName) {
    return ((JsPropertyMap<T>) $refs).get(refName);
  }

  @JsOverlay
  public final <T> JsArray<T> $refArray(String refName) {
    return ((JsPropertyMap<JsArray<T>>) $refs).get(refName);
  }

  @JsOverlay
  public final boolean $isRefArray(String refName) {
    return ((JsPropertyMap<Object>) $refs).get(refName) instanceof JsArray;
  }

  @JsOverlay
  public final JsPropertyMap<JsArray<VNode>> $slots() {
    return $slots;
  }

  @JsOverlay
  public final JsPropertyMap<ScopedSlot> $scopedSlots() {
    return $scopedSlots;
  }

  @JsOverlay
  public final boolean $isServer() {
    return $isServer;
  }

  @JsOverlay
  public final Object $ssrContext() {
    return $ssrContext;
  }

  @JsOverlay
  public final Object $vnode() {
    return $vnode;
  }

  @JsOverlay
  public final JsPropertyMap<String> $attrs() {
    return $attrs;
  }

  @JsOverlay
  public final Object $listeners() {
    return $listeners;
  }

  @JsOverlay
  public final Function $listener(String key) {
    return ((JsPropertyMap<Function>) $listeners).get(key);
  }

  @JsOverlay
  public final JsArray<Function> $listenerArray(String key) {
    return ((JsPropertyMap<JsArray<Function>>) $listeners).get(key);
  }

  @JsOverlay
  public final boolean $isListenerArray(String key) {
    return ((JsPropertyMap<Object>) $listeners).get(key) instanceof JsArray;
  }

  @JsOverlay
  public final <T> T $computed(String propertyName) {
    JsPropertyMap<T> map = Js.cast(this);
    return map.get(propertyName);
  }
}