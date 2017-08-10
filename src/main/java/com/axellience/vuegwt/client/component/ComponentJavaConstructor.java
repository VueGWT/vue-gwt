package com.axellience.vuegwt.client.component;

import com.axellience.vuegwt.client.tools.JsTools;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Function")
public class ComponentJavaConstructor<T extends VueComponent>
{
    @JsOverlay
    public final ComponentJavaPrototype<T> getPrototype()
    {
        return JsTools.get(this, "prototype");
    }
}
