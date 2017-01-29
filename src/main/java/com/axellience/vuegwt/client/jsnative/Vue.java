package com.axellience.vuegwt.client.jsnative;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.VueComponentFactory;
import com.axellience.vuegwt.client.VueDirective;
import com.axellience.vuegwt.client.definitions.VueComponentDefinition;
import com.axellience.vuegwt.client.definitions.VueDirectiveDefinition;
import com.google.gwt.dom.client.Element;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import static com.axellience.vuegwt.client.definitions.VueComponentDefinitionCache.getComponentDefinitionForClass;

/**
 * JsInterop representation of the main Vue instance
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class Vue
{
    /**
     * Method to attach a Vue Component to a DOM element
     * Equivalent to new Vue({el: element, ...}) in Vue.JS
     * @param vueComponentClass The class of the Component to create
     * here to initialise your app.
     */
    @JsOverlay
    public static <T extends VueComponent> T attach(String element, Class<T> vueComponentClass)
    {
        VueComponentDefinition componentDefinition =
            getComponentDefinitionForClass(vueComponentClass);
        componentDefinition.setEl(element);

        return VueGwtTools.createVueInstance(componentDefinition);
    }

    /**
     * Method to attach a Vue Component to a DOM element
     * Equivalent to new Vue({el: element, ...}) in Vue.JS
     * @param vueComponentClass The class of the Component to create
     * here to initialise your app.
     */
    @JsOverlay
    public static <T extends VueComponent> T attach(Element element, Class<T> vueComponentClass)
    {
        VueComponentDefinition componentDefinition =
            getComponentDefinitionForClass(vueComponentClass);
        componentDefinition.setEl(element);

        return VueGwtTools.createVueInstance(componentDefinition);
    }


    @JsOverlay
    public static VueComponentFactory extend(
        Class<? extends VueComponent> vueComponentClass)
    {
        JsObject extendedVueClass = extend(getComponentDefinitionForClass(vueComponentClass));
        return new VueComponentFactory(extendedVueClass);
    }

    /**
     * Register a component globally
     * It will be usable anywhere in your app.
     * This should be called before instantiating your app.
     * @param vueComponentClass The component to register. You should inherit from VueComponent and
     * pass
     * an instance of your component here.
     */
    @JsOverlay
    public static void component(Class<? extends VueComponent> vueComponentClass)
    {
        Vue.component(
            VueGwtTools.componentToTagName(vueComponentClass),
            getComponentDefinitionForClass(vueComponentClass)
        );
    }

    /**
     * Register a directive globally
     * It will be usable anywhere in your app.
     * This should be called before instantiating your app.
     * @param vueDirective The directive to register. You should inherit from VueDirective and pass
     * an instance of your directive here.
     */
    @JsOverlay
    public static void directive(VueDirective vueDirective)
    {

    }

    private static native JsObject extend(VueComponentDefinition componentDefinition);

    private static native void component(String componentName,
        VueComponentDefinition componentDefinition);

    private static native void directive(String directiveName, VueDirectiveDefinition vueDirective);
}
