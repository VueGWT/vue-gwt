package com.axellience.vuegwt.client.jsnative;

import com.axellience.vuegwt.client.VueApp;
import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.VueDirective;
import com.axellience.vuegwt.client.VueModel;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * JsInterop representation of the main Vue instance
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class Vue
{
    private Vue(VueModel vueApp) {

    }

    /**
     * Method to create a Vue app instance
     * @param vueApp The app to create. You should inherit from VueApp and pass an instance here to initialise your app.
     */
    @JsOverlay
    public static void app(VueApp vueApp) {
        new Vue(VueGwtTools.convertFromJavaToVueModel(vueApp));
    }

    /**
     * Register a component globally
     * It will be usable anywhere in your app.
     * This should be called before instantiating your app.
     * @param vueComponent The component to register. You should inherit from VueComponent and pass an instance of your component here.
     */
    @JsOverlay
    public static void component(VueComponent vueComponent) {
        Vue.component(vueComponent.getName(), VueGwtTools.convertFromJavaToVueModel(vueComponent));
    }

    /**
     * Register a directive globally
     * It will be usable anywhere in your app.
     * This should be called before instantiating your app.
     * @param vueDirective The directive to register. You should inherit from VueDirective and pass an instance of your directive here.
     */
    @JsOverlay
    public static void directive(VueDirective vueDirective) {
        Vue.directive(vueDirective.getName(), VueGwtTools.convertFromJavaToVueDirective(vueDirective));
    }

    private static native void component(String componentName, VueModel vueComponent);

    private static native void directive(String directiveName, VueDirective vueDirective);
}
