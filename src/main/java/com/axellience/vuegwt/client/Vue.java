package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.component.VueComponentFactory;
import com.axellience.vuegwt.client.component.hooks.HasCreated;
import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.client.component.options.functions.OnEvent;
import com.axellience.vuegwt.client.component.options.functions.OnNextTick;
import com.axellience.vuegwt.client.component.options.watch.ChangeTrigger;
import com.axellience.vuegwt.client.component.options.watch.OnValueChange;
import com.axellience.vuegwt.client.component.options.watch.WatcherRegistration;
import com.axellience.vuegwt.client.directive.VueDirective;
import com.axellience.vuegwt.client.directive.options.VueDirectiveOptions;
import com.axellience.vuegwt.client.jsnative.jstypes.JsArray;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.tools.VueGwtTools;
import com.axellience.vuegwt.client.tools.VueGwtToolsInjector;
import com.axellience.vuegwt.client.vnode.ScopedSlot;
import com.axellience.vuegwt.client.vnode.VNode;
import com.axellience.vuegwt.client.vue.VueConfig;
import com.google.gwt.dom.client.Element;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

import static com.axellience.vuegwt.client.VueOptionsCache.getComponentOptions;
import static com.axellience.vuegwt.client.VueOptionsCache.getDirectiveOptions;

/**
 * The Java representation of a Vue Component.
 * Whenever you want to add a component to your application you should extends this class and add
 * the
 * {@link com.axellience.vuegwt.jsr69.component.annotations.Component} annotation.
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public abstract class Vue implements HasCreated
{
    static
    {
        // Inject the JS tools in the page
        VueGwtToolsInjector.inject();
    }

    public static VueConfig config;

    @JsProperty public JsObject $data;
    @JsProperty public Element $el;
    @JsProperty public VueComponentOptions $options;
    @JsProperty public Vue $parent;
    @JsProperty public Vue $root;
    @JsProperty public JsArray<Vue> $children;
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

    /**
     * Create a {@link Vue} instance and attach it to a DOM element.
     * Equivalent to new Vue({el: element, ...}) in Vue.js.
     * @param element CSS selector for the element to attach in
     * @param vueComponentClass The class of the Component to create
     * @param <T> {@link Vue} we want to attach
     * @return The created and attached instance of our Component
     */
    @JsOverlay
    public static <T extends Vue> T attach(String element, Class<T> vueComponentClass)
    {
        VueComponentOptions<T> componentDefinition =
            getComponentOptions(vueComponentClass);
        componentDefinition.setEl(element);

        return VueGwtTools.createVueInstance(componentDefinition);
    }

    /**
     * Create a {@link Vue} instance and attach it to a DOM element.
     * Equivalent to new Vue({el: element, ...}) in Vue.js.
     * @param element DOM Element we want to attach our component in
     * @param vueComponentClass The class of the Component to create
     * @param <T> {@link Vue} we want to attach
     * @return The created and attached instance of our Component
     */
    @JsOverlay
    public static <T extends Vue> T attach(Element element, Class<T> vueComponentClass)
    {
        VueComponentOptions<T> componentDefinition =
            getComponentOptions(vueComponentClass);
        componentDefinition.setEl(element);

        return VueGwtTools.createVueInstance(componentDefinition);
    }

    /**
     * Extend the base Vue Class with your {@link VueComponentOptions}.
     * Equivalent to Vue.extend({}) in Vue.js.
     * @param vueComponentClass The class of the Component to use
     * @param <T> {@link Vue} we want to attach
     * @return A factory that can be used to create instance of your VueComponent
     */
    @JsOverlay
    public static <T extends Vue> VueComponentFactory<T> extend(Class<T> vueComponentClass)
    {
        JsObject extendedVueClass = extend(getComponentOptions(vueComponentClass));
        return new VueComponentFactory<>(extendedVueClass);
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
        Vue.component(id, getComponentOptions(vueComponentClass));
    }

    /**
     * Return the factory for a given registered component ID.
     * @param id Id of the {@link Vue} we want the factory of
     * @return A {@link VueComponentFactory} that you can use to build instance of your {@link Vue}
     */
    @JsOverlay
    public static VueComponentFactory component(String id)
    {
        JsObject extendedVueClass = getRegisteredComponent(id);
        return new VueComponentFactory(extendedVueClass);
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

    private static native void component(String id, VueComponentOptions componentOptions);

    private static native void directive(String name, VueDirectiveOptions directiveOptions);

    private static native JsObject extend(VueComponentOptions componentOptions);

    @JsMethod(name = "component")
    private static native JsObject getRegisteredComponent(String id);

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
    public native Vue $mount();

    @JsMethod
    public native Vue $mount(Element element);

    @JsMethod
    public native Vue $mount(String element);

    @JsMethod
    public native Vue $mount(Element element, boolean hydrating);

    @JsMethod
    public native Vue $mount(String element, boolean hydrating);

    @JsMethod
    public native void $forceUpdate();

    @JsMethod
    public native void $nextTick(OnNextTick onNextTick);

    @JsMethod
    public native void $destroy();
}