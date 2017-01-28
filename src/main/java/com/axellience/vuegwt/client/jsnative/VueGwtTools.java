package com.axellience.vuegwt.client.jsnative;

import com.axellience.vuegwt.client.OnEvent;
import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.VueDirective;
import com.axellience.vuegwt.client.jsnative.definitions.VueDirectiveDefinition;
import com.axellience.vuegwt.client.jsnative.definitions.ComponentDefinition;
import jsinterop.annotations.JsMethod;

import com.google.gwt.regexp.shared.RegExp;

/**
 * This object provides methods used to make our Java components compatible with Vue.JS
 */
public class VueGwtTools
{
    private static RegExp camelCasePattern = RegExp.compile("([a-z])([A-Z]+)", "g");
    private static RegExp componentEnd = RegExp.compile("Component$");
    private static RegExp directiveEnd = RegExp.compile("Directive$");

    @JsMethod(namespace = "vueGwt")
    public static native void vue$emit(VueComponent vueComponent, String name, Object value);

    @JsMethod(namespace = "vueGwt")
    public static native void vue$on(VueComponent vueComponent, String eventName, OnEvent onEvent);

    @JsMethod(namespace = "vueGwt")
    public static native Object getGwtObjectMethod(Object javaObject, String javaName);

    /**
     * Return the default name to register a component based on it's class name
     * The name of the tag is the name of the component converted to kebab-case
     * If the component class ends with "Component", this part is ignored
     * @param vueComponentClass
     */
    public static String componentToTagName(Class<? extends VueComponent> vueComponentClass) {
        String name = vueComponentClass.getSimpleName();
        // Drop "Component" at the end of the class name
        name = componentEnd.replace(name, "");
        // Convert from CamelCase to kebab-case
        return camelCasePattern.replace(name, "$1-$2").toLowerCase();
    }

    /**
     * Return the default name to register a directive based on it's class name
     * The name of the tag is the name of the component converted to kebab-case
     * If the component class ends with "Directive", this part is ignored
     * @param vueDirective
     */
    public static String directiveToTagName(VueDirective vueDirective) {
        String name = vueDirective.getClass().getSimpleName();
        // Drop "Component" at the end of the class name
        name = directiveEnd.replace(name, "");
        // Convert from CamelCase to kebab-case
        return camelCasePattern.replace(name, "$1-$2").toLowerCase();
    }
}
