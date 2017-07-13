package com.axellience.vuegwt.client.jsnative;

import com.axellience.vuegwt.client.options.VueComponentOptions;
import com.axellience.vuegwt.client.options.VueDirectiveOptions;
import com.axellience.vuegwt.client.options.VueOptionsCache;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType(name = "VueGwt", namespace = JsPackage.GLOBAL)
public class VueGwt
{
    public static VueComponentOptions getComponentOptions(String componentClassCanonicalName)
    {
        return VueOptionsCache.getComponentOptions(componentClassCanonicalName);
    }

    public static VueDirectiveOptions getDirectiveOptions(String directiveClassCanonicalName)
    {
        return VueOptionsCache.getDirectiveOptions(directiveClassCanonicalName);
    }
}
