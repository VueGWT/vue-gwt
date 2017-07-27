package com.axellience.vuegwt.client.component.jstype;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.VueComponentPrototype;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.tools.JsTools;
import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsOverlay;

/**
 * @author Adrien Baron
 */
@JsFunction
@FunctionalInterface
public interface VueComponentJsTypeConstructor<T extends VueComponent>
{
    <K extends T> K instanciate(Object... arguments);

    @JsOverlay
    default VueComponentPrototype<T> getComponentPrototype()
    {
        JsObject prototype = JsTools.get(this, "prototype");
        return (VueComponentPrototype<T>) JsObject.getPrototypeOf(prototype);
    }

    @JsOverlay
    default void apply(Object context, Object... arguments)
    {
        JsTools.call(this, context, arguments);
    }
}
