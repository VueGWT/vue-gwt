package com.axellience.vuegwt.core.client.tools;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.vue.VueJsConstructor;
import elemental2.core.Function;
import elemental2.core.JsObject;
import jsinterop.annotations.JsFunction;
import jsinterop.base.JsPropertyMap;

/**
 * This object provides utils methods for VueGWT internal processing
 * @author Adrien Baron
 */
public class VueGWTTools
{
    /**
     * Copy a Java class prototype to a VueComponent declaration. This allows
     * VueComponent created by Vue to pass as an instance of the {@link IsVueComponent}
     * class they represent.
     * @param extendedVueJsConstructor The Vue.js constructor function to extend
     * @param javaComponentPrototype The {@link IsVueComponent} class JS prototype to
     * extend with
     * @param <T> The type of the VueComponent
     */
    public static <T extends IsVueComponent> void extendVueConstructorWithJavaPrototype(
        VueJsConstructor<T> extendedVueJsConstructor,
        JsPropertyMap<Object> javaComponentPrototype)
    {
        JsPropertyMap vueProto =
            (JsPropertyMap) ((JsPropertyMap) extendedVueJsConstructor).get("prototype");
        JsObject vueProtoJsObject = ((JsObject) vueProto);
        javaComponentPrototype.forEach(protoProp -> {
            if (!vueProtoJsObject.hasOwnProperty(protoProp))
                vueProto.set(protoProp, javaComponentPrototype.get(protoProp));
        });
    }

    /**
     * Proxy a method call to be warned when it called. This requires the
     * function to be JsInterop (name shouldn't change at runtime). This used to
     * observe Java Collections/Map. It won't be necessary in future versions of
     * Vue.js based on ES6 proxies.
     * @param object The object to observe
     * @param methodName The name of the method to proxify
     * @param afterMethodCall A callback called each time after the method has
     * been executed
     * @param <T> Type of the object the we Proxy
     */
    public static <T> void wrapMethod(T object, String methodName,
        AfterMethodCall<T> afterMethodCall)
    {
        Function method = (Function) ((JsPropertyMap) object).get(methodName);

        WrappingFunction wrappingFunction = args -> {
            Object result = method.apply(object, args);
            afterMethodCall.execute(object, methodName, result, args);
            return result;
        };

        ((JsPropertyMap) object).set(methodName, wrappingFunction);
    }

    @FunctionalInterface
    @JsFunction
    private interface WrappingFunction
    {
        Object call(Object... args);
    }

    /**
     * Return a "deep" value in a given object by following an expression in the
     * form: "parent.child.property". This only works if all the chain is
     * exposed using JsInterop.
     * @param object The root object to get on
     * @param path The path to follow
     * @param <T> The type of object we get in return
     * @return The object at the end of the chain
     */
    public static <T> T getDeepValue(Object object, String path)
    {
        JsPropertyMap objectMap = (JsPropertyMap) object;
        String[] pathSplit = path.split("\\.");
        for (String s : pathSplit)
        {
            objectMap = (JsPropertyMap) objectMap.get(s);
        }
        return (T) objectMap;
    }

    /**
     * Convert a template expression to String while keeping null values
     * @param expressionValue The value of the expression from the template
     * @return Null if passed null, the value to String otherwise
     */
    public static String templateExpressionToString(Object expressionValue)
    {
        return expressionValue == null ? null : expressionValue + "";
    }
}
