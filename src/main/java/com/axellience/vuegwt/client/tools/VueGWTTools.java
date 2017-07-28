package com.axellience.vuegwt.client.tools;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.jstype.VueComponentJsTypeConstructor;
import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.client.directive.VueDirective;
import com.axellience.vuegwt.client.jsnative.jstypes.JsArray;
import com.axellience.vuegwt.client.vue.VueConstructor;
import com.google.gwt.regexp.shared.RegExp;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsType;

/**
 * This object provides utils methods for VueGWT internal processing
 * @author Adrien Baron
 */
@JsType(namespace = "VueGWT", name = "tools")
public class VueGWTTools
{
    private static RegExp camelCasePattern = RegExp.compile("([a-z])([A-Z]+)", "g");
    private static RegExp componentEnd = RegExp.compile("Component$");
    private static RegExp directiveEnd = RegExp.compile("Directive$");

    public static native <T extends VueComponent> T createInstanceForVueClass(
        VueConstructor<T> vueClass);

    public static native <T extends VueComponent, K extends T> VueConstructor<K> extendVueClass(
        VueConstructor<T> vueClassToExtend, VueComponentOptions<K> vueComponentOptions);

    @JsMethod(name = "wrapMethodWithBefore")
    public static native <T> String wrapMethod(T object, String methodName,
        BeforeMethodCall<T> afterMethodCall);

    @JsMethod(name = "wrapMethodWithAfter")
    public static native <T> String wrapMethod(T object, String methodName,
        AfterMethodCall<T> afterMethodCall);

    public static native <T> String wrapMethod(T object, String methodName,
        BeforeMethodCall<T> beforeMethodCall, AfterMethodCall<T> afterMethodCall);

    public static native <T extends VueComponent> void mergeVueConstructorWithJavaComponent(
        VueConstructor<T> extendedVueConstructor,
        VueComponentJsTypeConstructor<T> jsTypeConstructor);

    public static native <T> JsArray<T> javaArrayToJsArray(Object[] javaArray);

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
}
