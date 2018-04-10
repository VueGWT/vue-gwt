package com.axellience.vuegwt.core.client;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.core.client.customelement.CustomElementOptions;
import com.axellience.vuegwt.core.client.customelement.VueCustomElementType;
import com.axellience.vuegwt.core.client.directive.options.VueDirectiveOptions;
import com.axellience.vuegwt.core.client.jsnative.jsfunctions.JsRunnable;
import com.axellience.vuegwt.core.client.tools.VueGWTTools;
import com.axellience.vuegwt.core.client.vue.VueConfig;
import com.axellience.vuegwt.core.client.vue.VueFactory;
import com.axellience.vuegwt.core.client.vue.VueJsConstructor;
import elemental2.dom.Element;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

import static com.axellience.vuegwt.core.client.VueGWT.createInstance;

/**
 * The Java representation of Vue.
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public abstract class Vue
{
    @JsProperty private static VueConfig config;

    /**
     * Create a {@link Vue} instance and mount it on a DOM element.
     * @param element CSS selector for the element to attach in
     * @param isVueComponentClass The Class of the Component to create
     * @param <T> {@link IsVueComponent} we want to attach
     * @return The created and attached instance of our Component
     */
    @JsOverlay
    public static <T extends IsVueComponent> T attach(String element, Class<T> isVueComponentClass)
    {
        T vueInstance = createInstance(isVueComponentClass);
        vueInstance.vue().$mount(element);
        return vueInstance;
    }

    /**
     * Create a {@link Vue} instance and mount it on a DOM element.
     * @param element CSS selector for the element to attach in
     * @param vueFactory The factory of the Component to create
     * @param <T> {@link IsVueComponent} we want to attach
     * @return The created and attached instance of our Component
     */
    @JsOverlay
    public static <T extends IsVueComponent> T attach(String element, VueFactory<T> vueFactory)
    {
        T vueInstance = vueFactory.create();
        vueInstance.vue().$mount(element);
        return vueInstance;
    }

    /**
     * Create a {@link Vue} instance and mount it on a DOM element.
     * @param element DOM Element we want to attach our component in
     * @param isVueComponentClass The Class of the Component to create
     * @param <T> {@link IsVueComponent} we want to attach
     * @return The created and attached instance of our Component
     */
    @JsOverlay
    public static <T extends IsVueComponent> T attach(Element element, Class<T> isVueComponentClass)
    {
        T vueInstance = createInstance(isVueComponentClass);
        vueInstance.vue().$mount(element);
        return vueInstance;
    }

    /**
     * Create a {@link Vue} instance and mount it on a DOM element.
     * @param element DOM Element we want to attach our component in
     * @param vueFactory The factory of the Component to create
     * @param <T> {@link IsVueComponent} we want to attach
     * @return The created and attached instance of our Component
     */
    @JsOverlay
    public static <T extends IsVueComponent> T attach(Element element, VueFactory<T> vueFactory)
    {
        T vueInstance = vueFactory.create();
        vueInstance.vue().$mount(element);
        return vueInstance;
    }

    /**
     * Register a {@link IsVueComponent} globally
     * @param id Id for our component in the templates
     * @param isVueComponentClass The Class of the {@link IsVueComponent} to create
     * @param <T> {@link IsVueComponent} we want to attach
     */
    @JsOverlay
    public static <T extends IsVueComponent> void component(String id, Class<T> isVueComponentClass)
    {
        component(id, VueGWT.getFactory(isVueComponentClass));
    }

    /**
     * Register a {@link IsVueComponent} globally
     * @param id Id for our component in the templates
     * @param vueFactory The factory of the Component to create
     * @param <T> {@link IsVueComponent} we want to attach
     */
    @JsOverlay
    public static <T extends IsVueComponent> void component(String id, VueFactory<T> vueFactory)
    {
        component(id, vueFactory.getJsConstructor());
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

    @JsOverlay
    public static <T extends IsVueComponent> VueJsConstructor<T> extendJavaComponent(
        VueComponentOptions<T> componentOptions)
    {
        VueJsConstructor<T> extendedVueJsConstructor = extend(componentOptions);
        VueGWTTools.extendVueConstructorWithJavaPrototype(extendedVueJsConstructor,
            componentOptions.getComponentJavaPrototype());

        return extendedVueJsConstructor;
    }

    @JsOverlay
    public static <T extends IsVueComponent> VueCustomElementType<T> customElement(
        String componentTag, Class<T> isVueComponentClass)
    {
        return Vue.customElement(componentTag, isVueComponentClass, new CustomElementOptions<>());
    }

    @JsOverlay
    public static <T extends IsVueComponent> VueCustomElementType<T> customElement(
        String componentTag, VueFactory<T> vueFactory)
    {
        return Vue.customElement(componentTag, vueFactory, new CustomElementOptions<>());
    }

    @JsOverlay
    public static <T extends IsVueComponent> VueCustomElementType<T> customElement(
        String componentTag, VueJsConstructor<T> vueJsConstructor)
    {
        return Vue.customElement(componentTag, vueJsConstructor, new CustomElementOptions<>());
    }

    @JsOverlay
    public static <T extends IsVueComponent> VueCustomElementType<T> customElement(
        String componentTag, Class<T> isVueComponentClass, CustomElementOptions<T> options)
    {
        return Vue.customElement(componentTag, VueGWT.getFactory(isVueComponentClass), options);
    }

    @JsOverlay
    public static <T extends IsVueComponent> VueCustomElementType<T> customElement(
        String componentTag, VueFactory<T> vueFactory, CustomElementOptions<T> options)
    {
        return Vue.customElement(componentTag, vueFactory.getJsConstructor(), options);
    }

    @JsOverlay
    public static <T extends IsVueComponent> VueCustomElementType<T> customElement(
        String componentTag, VueJsConstructor<T> vueJsConstructor, CustomElementOptions<T> options)
    {
        VueCustomElementLibInjector.ensureInjected();
        return Vue.customElementNative(componentTag, vueJsConstructor, options);
    }

    // @formatter:off
    public static native <T extends IsVueComponent> VueJsConstructor<T> extend(VueComponentOptions<T> componentOptions);

    public static native void nextTick(JsRunnable callback);

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

    @JsMethod(name = "customElement")
    public static native <T extends IsVueComponent> VueCustomElementType<T> customElementNative(String componentTag, VueJsConstructor<T> vueJsConstructor, CustomElementOptions options);

    public static native <T extends IsVueComponent> void component(String id, VueComponentOptions<T> componentOptions);
    public static native <T extends IsVueComponent> void component(String id, VueJsConstructor<T> vueJsConstructor);
    public static native <T extends IsVueComponent> VueJsConstructor<T> component(String id);
    // @formatter:on
}