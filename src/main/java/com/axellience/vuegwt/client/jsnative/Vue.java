package com.axellience.vuegwt.client.jsnative;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.VueDirective;
import com.axellience.vuegwt.client.jsnative.definitions.VueComponentDefinition;
import com.axellience.vuegwt.client.jsnative.definitions.VueDirectiveDefinition;
import com.google.gwt.dom.client.Element;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * JsInterop representation of the main Vue instance
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class Vue
{
    private Vue(VueComponentDefinition vueComponent)
    {

    }

    /**
     * Method to attach a Vue Component to a DOM element
     * Equivalent to new Vue({el: element, ...}) in Vue.JS
     * @param vueComponent The co to create. You should inherit from VueApp and pass an instance
     * here to initialise your app.
     */
    @JsOverlay
    public static void attach(String element, VueComponent vueComponent)
    {
        vueComponent.setEl(element);
        new Vue(VueGwtTools.javaComponentToVueComponentDefinition(vueComponent));
    }

    /**
     * Method to attach a Vue Component to a DOM element
     * Equivalent to new Vue({el: element, ...}) in Vue.JS
     * @param vueComponent The co to create. You should inherit from VueApp and pass an instance
     * here to initialise your app.
     */
    @JsOverlay
    public static void attach(Element element, VueComponent vueComponent)
    {
        vueComponent.setEl(element);
        new Vue(VueGwtTools.javaComponentToVueComponentDefinition(vueComponent));
    }

    /**
     * Register a component globally
     * It will be usable anywhere in your app.
     * This should be called before instantiating your app.
     * @param vueComponent The component to register. You should inherit from VueComponent and pass
     * an instance of your component here.
     */
    @JsOverlay
    public static void component(VueComponent vueComponent)
    {
        Vue.component(
            VueGwtTools.componentToTagName(vueComponent),
            VueGwtTools.javaComponentToVueComponentDefinition(vueComponent)
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
        Vue.directive(
            VueGwtTools.directiveToTagName(vueDirective),
            VueGwtTools.javaDirectiveToVueDirectiveDefinition(vueDirective)
        );
    }

    private static native void component(String componentName, VueComponentDefinition vueComponent);

    private static native void directive(String directiveName, VueDirectiveDefinition vueDirective);
}
