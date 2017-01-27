package com.axellience.vuegwt.client.jsnative;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.VueDirective;
import com.axellience.vuegwt.client.jsnative.definitions.VueDirectiveDefinition;
import com.axellience.vuegwt.client.jsnative.definitions.ComponentDefinition;
import com.google.gwt.dom.client.Element;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import java.util.HashMap;
import java.util.Map;

/**
 * JsInterop representation of the main Vue instance
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class Vue
{
    @JsOverlay
    private static Map<Class<? extends VueComponent>, JsObject>
        jsComponentDefinitionsCache = new HashMap<>();

    private Vue(JsObject vueComponent)
    {

    }

    @JsOverlay
    public static void registerComponent(Class<? extends VueComponent> vueComponentClass, ComponentDefinition componentDefinition)
    {
        JsObject jsComponentDefinition = VueGwtTools.javaComponentDefinitionToJs(componentDefinition);
        jsComponentDefinitionsCache.put(vueComponentClass, jsComponentDefinition);
    }

    @JsOverlay
    public static JsObject getJsComponentDefinitionForClass(
        Class<? extends VueComponent> vueComponentClass)
    {
        JsObject jsComponentDefinition = jsComponentDefinitionsCache.get(vueComponentClass);
        if (jsComponentDefinition != null)
            return jsComponentDefinition;

        throw new RuntimeException(
            "Couldn't find the given Component " +
                vueComponentClass.getCanonicalName() +
                ". Are you sure annotations are being processed?");
    }

    /**
     * Method to attach a Vue Component to a DOM element
     * Equivalent to new Vue({el: element, ...}) in Vue.JS
     * @param vueComponentClass The co to create. You should inherit from VueApp and pass an
     * instance
     * here to initialise your app.
     */
    @JsOverlay
    public static void attach(String element, Class<? extends VueComponent> vueComponentClass)
    {
        JsObject jsComponentDefinition =
            getJsComponentDefinitionForClass(vueComponentClass);
        jsComponentDefinition.set("el", element);

        new Vue(jsComponentDefinition);
    }

    /**
     * Method to attach a Vue Component to a DOM element
     * Equivalent to new Vue({el: element, ...}) in Vue.JS
     * @param vueComponentClass The co to create. You should inherit from VueApp and pass an
     * instance
     * here to initialise your app.
     */
    @JsOverlay
    public static void attach(Element element, Class<? extends VueComponent> vueComponentClass)
    {
        JsObject jsComponentDefinition =
            getJsComponentDefinitionForClass(vueComponentClass);
        jsComponentDefinition.set("el", element);

        new Vue(jsComponentDefinition);
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
        JsObject jsComponentDefinition =
            getJsComponentDefinitionForClass(vueComponentClass);

        Vue.component(VueGwtTools.componentToTagName(vueComponentClass), jsComponentDefinition);
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
        Vue.directive(VueGwtTools.directiveToTagName(vueDirective),
            VueGwtTools.javaDirectiveToVueDirectiveDefinition(vueDirective)
        );
    }

    private static native void component(String componentName, JsObject vueComponent);

    private static native void directive(String directiveName, VueDirectiveDefinition vueDirective);
}
