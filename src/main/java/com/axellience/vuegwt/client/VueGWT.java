package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.vue.VueConstructor;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType(namespace = JsPackage.GLOBAL)
public class VueGWT
{
    private static final JsObject<VueConstructor<? extends Vue>> componentConstructors =
        new JsObject<>();

    public static <T extends Vue> void register(String qualifiedName,
        VueConstructor<T> vueConstructor)
    {
        componentConstructors.set(qualifiedName, vueConstructor);
    }

    public static VueConstructor<? extends Vue> get(String qualifiedName)
    {
        return componentConstructors.get(qualifiedName);
    }
}
