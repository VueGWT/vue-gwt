package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.client.directive.options.VueDirectiveOptions;
import com.axellience.vuegwt.client.jsnative.jsfunctions.JsRunnable;
import com.axellience.vuegwt.client.jsnative.jstypes.JsArray;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.tools.VueGWTTools;
import com.axellience.vuegwt.client.vue.VueConfig;
import com.axellience.vuegwt.client.vue.VueJsConstructor;
import com.axellience.vuegwt.client.vue.VueFactory;
import com.google.gwt.dom.client.Element;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

import static com.axellience.vuegwt.client.VueGWT.createInstance;

/**
 * The Java representation of Vue.
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public abstract class Vue extends JsObject
{
    @JsProperty private static VueConfig config;

    /**
     * Create a {@link Vue} instance and mount it on a DOM element.
     * @param element CSS selector for the element to attach in
     * @param vueFactory The factory of the Component to create
     * @param <T> {@link Vue} we want to attach
     * @return The created and attached instance of our Component
     */
    @JsOverlay
    public static <T extends VueComponent> T attach(String element, VueFactory<T> vueFactory)
    {
        T vueInstance = vueFactory.create();
        vueInstance.$mount(element);
        return vueInstance;
    }

    /**
     * Create a {@link Vue} instance and mount it on a DOM element.
     * @param element CSS selector for the element to attach in
     * @param vueComponentClass The Class of the Component to create
     * @param <T> {@link Vue} we want to attach
     * @return The created and attached instance of our Component
     */
    @JsOverlay
    public static <T extends VueComponent> T attach(String element, Class<T> vueComponentClass)
    {
        T vueInstance = createInstance(vueComponentClass);
        vueInstance.$mount(element);
        return vueInstance;
    }

    /**
     * Create a {@link Vue} instance and mount it on a DOM element.
     * @param element DOM Element we want to attach our component in
     * @param vueFactory The factory of the Component to create
     * @param <T> {@link Vue} we want to attach
     * @return The created and attached instance of our Component
     */
    @JsOverlay
    public static <T extends VueComponent> T attach(Element element, VueFactory<T> vueFactory)
    {
        T vueInstance = vueFactory.create();
        vueInstance.$mount(element);
        return vueInstance;
    }

    /**
     * Create a {@link Vue} instance and mount it on a DOM element.
     * @param element DOM Element we want to attach our component in
     * @param vueComponentClass The Class of the Component to create
     * @param <T> {@link Vue} we want to attach
     * @return The created and attached instance of our Component
     */
    @JsOverlay
    public static <T extends VueComponent> T attach(Element element, Class<T> vueComponentClass)
    {
        T vueInstance = createInstance(vueComponentClass);
        vueInstance.$mount(element);
        return vueInstance;
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
    public static <T extends VueComponent> VueJsConstructor<T> extendJavaComponent(
        VueComponentOptions<T> componentOptions)
    {
        VueJsConstructor<T> extendedVueJsConstructor = extend(componentOptions);
        VueGWTTools.extendVueConstructorWithJavaComponent(extendedVueJsConstructor,
            componentOptions.getComponentWithTemplate());

        return extendedVueJsConstructor;
    }

    @JsOverlay
    public static <T extends VueComponent> void component(String id, VueFactory<T> vueFactory)
    {
        component(id, vueFactory.getJsConstructor());
    }

    // @formatter:off
    public static native <T extends VueComponent> VueJsConstructor<T> extend(VueComponentOptions<T> componentOptions);

    public static native void nextTick(JsRunnable callback, JsArray context);

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

    public static native <T extends VueComponent> void component(String id, VueComponentOptions<T> componentOptions);
    public static native <T extends VueComponent> void component(String id, VueJsConstructor<T> vueJsConstructor);
    public static native <T extends VueComponent> VueJsConstructor<T> component(String id);
    // @formatter:on
}