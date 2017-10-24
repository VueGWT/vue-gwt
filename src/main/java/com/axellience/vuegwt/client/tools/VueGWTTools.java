package com.axellience.vuegwt.client.tools;

import com.axellience.vuegwt.client.component.ComponentJavaPrototype;
import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.directive.VueDirective;
import com.axellience.vuegwt.client.vue.VueJsConstructor;
import com.google.gwt.regexp.shared.RegExp;
import elemental2.core.Function;
import jsinterop.annotations.JsFunction;
import jsinterop.base.JsPropertyMap;

/**
 * This object provides utils methods for VueGWT internal processing
 * @author Adrien Baron
 */
public class VueGWTTools
{
    private static RegExp camelCasePattern = RegExp.compile("([a-z])([A-Z]+)", "g");
    private static RegExp componentEnd = RegExp.compile("Component$");
    private static RegExp directiveEnd = RegExp.compile("Directive$");

    public static <T> void wrapMethod(T object, String methodName, AfterMethodCall<T> afterMethodCall)
    {
        Function method = (Function) ((JsPropertyMap) object).get(methodName);

        WrappingFunction wrappingFunction = args -> {
            Object result = method.apply(object, args);
            afterMethodCall.execute(object, methodName, result, args);
            return result;
        };

        ((JsPropertyMap) object).set(methodName, wrappingFunction);
    }

    public static <T extends VueComponent> void extendVueConstructorWithJavaPrototype(
        VueJsConstructor<T> extendedVueJsConstructor,
        ComponentJavaPrototype<T> componentJavaPrototype)
    {
        JsPropertyMap vueProto =
            (JsPropertyMap) ((JsPropertyMap) extendedVueJsConstructor).get("prototype");
        componentJavaPrototype.forEach(protoProp -> {
            if (!vueProto.has(protoProp))
                vueProto.set(protoProp, componentJavaPrototype.get(protoProp));
        });
    }

    /**
     * Return the default name to register a component based on it's class name.
     * The name of the tag is the name of the component converted to kebab-case.
     * If the component class ends with "Component", this part is ignored.
     * @param componentClassName The class name of the {@link VueComponent}
     * @return The name of the component
     */
    public static String componentToTagName(String componentClassName)
    {
        // Drop "Component" at the end of the class name
        componentClassName = componentEnd.replace(componentClassName, "");
        // Convert from CamelCase to kebab-case
        return camelCasePattern.replace(componentClassName, "$1-$2").toLowerCase();
    }

    /**
     * Return the default name to register a directive based on it's class name
     * The name of the tag is the name of the component converted to kebab-case
     * If the component class ends with "Directive", this part is ignored
     * @param directiveClassName The class name of the {@link VueDirective}
     * @return The name of the directive
     */
    public static String directiveToTagName(String directiveClassName)
    {
        // Drop "Component" at the end of the class name
        directiveClassName = directiveEnd.replace(directiveClassName, "");
        // Convert from CamelCase to kebab-case
        return camelCasePattern.replace(directiveClassName, "$1-$2").toLowerCase();
    }

    @FunctionalInterface
    @JsFunction
    private interface WrappingFunction
    {
        Object call(Object... args);
    }
}
