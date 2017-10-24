package com.axellience.vuegwt.client.component;

import com.axellience.vuegwt.client.tools.JsUtils;
import elemental2.core.Function;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Function")
public class ComponentJavaConstructor<T extends VueComponent> extends Function
{
    @JsOverlay
    public final ComponentJavaPrototype<T> getPrototype()
    {
        return JsUtils.get(this, "prototype");
    }
}
