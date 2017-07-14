package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.VueComponentFactory;
import com.axellience.vuegwt.client.directive.VueDirective;
import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.client.directive.options.VueDirectiveOptions;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.vue.VueConfig;
import com.axellience.vuegwt.client.tools.VueGwtTools;
import com.google.gwt.dom.client.Element;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import static com.axellience.vuegwt.client.VueOptionsCache.getComponentOptions;
import static com.axellience.vuegwt.client.VueOptionsCache.getDirectiveOptions;

/**
 * JsInterop representation of the main Vue instance.<br>
 * Provide some methods that are specific to VueGWT.
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class Vue
{
    public static VueConfig config;

    /**
     * Create a {@link VueComponent} instance and attach it to a DOM element.
     * Equivalent to new Vue({el: element, ...}) in Vue.js.
     * @param element CSS selector for the element to attach in
     * @param vueComponentClass The class of the Component to create
     * @param <T> {@link VueComponent} we want to attach
     * @return The created and attached instance of our Component
     */
    @JsOverlay
    public static <T extends VueComponent> T attach(String element, Class<T> vueComponentClass)
    {
        VueComponentOptions<T> componentDefinition =
            getComponentOptions(vueComponentClass);
        componentDefinition.setEl(element);

        return VueGwtTools.createVueInstance(componentDefinition);
    }

    /**
     * Create a {@link VueComponent} instance and attach it to a DOM element.
     * Equivalent to new Vue({el: element, ...}) in Vue.js.
     * @param element DOM Element we want to attach our component in
     * @param vueComponentClass The class of the Component to create
     * @param <T> {@link VueComponent} we want to attach
     * @return The created and attached instance of our Component
     */
    @JsOverlay
    public static <T extends VueComponent> T attach(Element element, Class<T> vueComponentClass)
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
     * @param <T> {@link VueComponent} we want to attach
     * @return A factory that can be used to create instance of your VueComponent
     */
    @JsOverlay
    public static <T extends VueComponent> VueComponentFactory<T> extend(Class<T> vueComponentClass)
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
    public static void component(Class<? extends VueComponent> vueComponentClass)
    {
        Vue.component(VueGwtTools.componentToTagName(vueComponentClass), vueComponentClass);
    }

    /**
     * Register a component globally.
     * It will be usable in any component of your app.
     * @param id Register under the given id
     * @param vueComponentClass The class of the Component to
     * @param <T> {@link VueComponent} we want to attach
     */
    @JsOverlay
    public static <T extends VueComponent> void component(String id, Class<T> vueComponentClass)
    {
        Vue.component(id, getComponentOptions(vueComponentClass));
    }

    /**
     * Return the factory for a given registered component ID.
     * @param id Id of the {@link VueComponent} we want the factory of
     * @return A {@link VueComponentFactory} that you can use to build instance of your {@link VueComponent}
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
}
