package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.component.hooks.HasCreated;
import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.client.component.options.functions.OnEvent;
import com.axellience.vuegwt.client.component.options.functions.OnNextTick;
import com.axellience.vuegwt.client.component.options.watch.ChangeTrigger;
import com.axellience.vuegwt.client.component.options.watch.OnValueChange;
import com.axellience.vuegwt.client.component.options.watch.WatcherRegistration;
import com.axellience.vuegwt.client.directive.VueDirective;
import com.axellience.vuegwt.client.directive.options.VueDirectiveOptions;
import com.axellience.vuegwt.client.jsnative.jsfunctions.JsSimpleFunction;
import com.axellience.vuegwt.client.jsnative.jstypes.JsArray;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.tools.VueGwtTools;
import com.axellience.vuegwt.client.tools.VueGwtToolsInjector;
import com.axellience.vuegwt.client.vnode.ScopedSlot;
import com.axellience.vuegwt.client.vnode.VNode;
import com.axellience.vuegwt.client.vue.VueConfig;
import com.axellience.vuegwt.client.vue.VueConstructor;
import com.google.gwt.dom.client.Element;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

import static com.axellience.vuegwt.client.VueGwtCache.getDirectiveOptions;
import static com.axellience.vuegwt.client.VueGwtCache.getVueConstructor;

/**
 * The Java representation of a Vue Component.
 * Whenever you want to add a component to your application you should extends this class and add
 * the
 * {@link com.axellience.vuegwt.jsr69.component.annotations.Component} annotation.
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public abstract class Vue extends JsObject implements HasCreated
{
    static
    {
        // Inject the JS tools in the page
        VueGwtToolsInjector.inject();
    }

    /* ---------------------------------------------

              Static Properties and Methods

      ---------------------------------------------*/

    @JsProperty private static VueConfig config;

    /**
     * Create a {@link Vue} instance and mount it on a DOM element.
     * @param element CSS selector for the element to attach in
     * @param vueComponentClass The class of the Component to create
     * @param <T> {@link Vue} we want to attach
     * @return The created and attached instance of our Component
     */
    @JsOverlay
    public static <T extends Vue> T attach(String element, Class<T> vueComponentClass)
    {
        VueConstructor<T> vueClass = getJsVueClass(vueComponentClass);
        T vueInstance = vueClass.instantiate();
        vueInstance.$mount(element);
        return vueInstance;
    }

    /**
     * Create a {@link Vue} instance and mount it on a DOM element.
     * @param element DOM Element we want to attach our component in
     * @param vueComponentClass The class of the Component to create
     * @param <T> {@link Vue} we want to attach
     * @return The created and attached instance of our Component
     */
    @JsOverlay
    public static <T extends Vue> T attach(Element element, Class<T> vueComponentClass)
    {
        VueConstructor<T> vueClass = getJsVueClass(vueComponentClass);
        T vueInstance = vueClass.instantiate();
        vueInstance.$mount(element);
        return vueInstance;
    }

    /**
     * Register a component globally.
     * It will be usable in any component of your app.
     * The name will be automatically computed based on the component class name.
     * @param vueComponentClass The class of the Component to register
     */
    @JsOverlay
    public static void component(Class<? extends Vue> vueComponentClass)
    {
        Vue.component(VueGwtTools.componentToTagName(vueComponentClass), vueComponentClass);
    }

    /**
     * Register a component globally.
     * It will be usable in any component of your app.
     * @param id Register under the given id
     * @param vueComponentClass The class of the Component to
     * @param <T> {@link Vue} we want to attach
     */
    @JsOverlay
    public static <T extends Vue> void component(String id, Class<T> vueComponentClass)
    {
        Vue.component(id, getVueConstructor(vueComponentClass));
    }

    /**
     * Register a directive globally.
     * It will be usable in any component of your app.
     * The name will be automatically computed based on the directive class name.
     * @param vueDirectiveClass The class of the {@link VueDirective} to register
     */
    @JsOverlay
    public static void directive(Class<? extends VueDirective> vueDirectiveClass)
    {
        Vue.directive(VueGwtTools.directiveToTagName(vueDirectiveClass), vueDirectiveClass);
    }

    /**
     * Register a directive globally.
     * It will be usable in any component of your app.
     * @param name Register under the given name
     * @param vueDirectiveClass The class of the {@link VueDirective} to register
     */
    @JsOverlay
    public static void directive(String name, Class<? extends VueDirective> vueDirectiveClass)
    {
        Vue.directive(name, getDirectiveOptions(vueDirectiveClass));
    }

    /**
     * Return a {@link VueConstructor} that allows you to create instances of your Vue.
     * @param vueComponentClass The class of the Component to use
     * @param <T> {@link Vue} we want to attach
     * @return A factory that can be used to create instance of your VueComponent
     */
    @JsOverlay
    public static <T extends Vue> VueConstructor<T> getJsVueClass(Class<T> vueComponentClass)
    {
        return getVueConstructor(vueComponentClass);
    }

    @JsOverlay
    public static VueConfig getConfig()
    {
        return config;
    }

    @JsOverlay
    public static void setConfig(VueConfig config)
    {
        Vue.config = config;
    }

    // @formatter:off
    public static native <T extends Vue> VueConstructor<T> extend(VueComponentOptions<T> componentOptions);

    public static native void nextTick(JsSimpleFunction callback, JsArray context);

    public static native <T> T set(Object object, String key, T value);
    public static native boolean set(Object object, String key, boolean value);
    public static native byte set(Object object, String key, byte value);
    public static native char set(Object object, String key, char value);
    public static native float set(Object object, String key, float value);
    public static native int set(Object object, String key, int value);
    public static native short set(Object object, String key, short value);
    public static native double set(Object object, String key, double value);
    public static native void delete(Object object, String key);

    public static native void directive(String id, VueDirectiveOptions directiveOptions);
    public static native VueDirectiveOptions directive(String id);

    public static native <T extends Vue> void component(String id, VueComponentOptions<T> componentOptions);
    public static native <T extends Vue> void component(String id, VueConstructor<T> vueConstructor);
    public static native <T extends Vue> VueConstructor<T> component(String id);
    // @formatter:on


    /* ---------------------------------------------

              Instance Properties and Methods

      ---------------------------------------------*/

    @JsProperty private JsObject $data;
    @JsProperty private Element $el;
    @JsProperty private VueComponentOptions $options;
    @JsProperty private Vue $parent;
    @JsProperty private Vue $root;
    @JsProperty private JsArray<Vue> $children;
    @JsProperty private Object $refs;
    @JsProperty private JsObject<VNode> $slots;
    @JsProperty private JsObject<ScopedSlot> $scopedSlots;
    @JsProperty private boolean $isServer;
    @JsProperty private Object $ssrContext;
    @JsProperty private Object $props;
    @JsProperty private Object $vnode;
    @JsProperty private JsObject<String> $attrs;
    @JsProperty private Object $listeners;

    @JsProperty public String _uid;

    // @formatter:off
    // Data
    public native WatcherRegistration $watch(String toWatch, OnValueChange onValueChange);
    public native WatcherRegistration $watch(ChangeTrigger changeTrigger,
        OnValueChange onValueChange);

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
    public native Vue $mount();
    public native Vue $mount(Element element);
    public native Vue $mount(String element);
    public native Vue $mount(Element element, boolean hydrating);
    public native Vue $mount(String element, boolean hydrating);
    public native void $forceUpdate();
    public native void $nextTick(OnNextTick onNextTick);
    public native void $destroy();
    // @formatter:on

    @JsOverlay
    public final JsObject $data()
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
    public final Vue $parent()
    {
        return $parent;
    }

    @JsOverlay
    public final Vue $root()
    {
        return $root;
    }

    @JsOverlay
    public final JsArray<Vue> $children()
    {
        return $children;
    }

    @JsOverlay
    public final Object $refs()
    {
        return $refs;
    }

    @JsOverlay
    public final JsObject<VNode> $slots()
    {
        return $slots;
    }

    @JsOverlay
    public final JsObject<ScopedSlot> $scopedSlots()
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
    public final JsObject<String> $attrs()
    {
        return $attrs;
    }

    @JsOverlay
    public final Object $listeners()
    {
        return $listeners;
    }
}