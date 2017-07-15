package com.axellience.vuegwt.client.vue;

import com.axellience.vuegwt.client.Vue;
import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.client.jsnative.jstypes.JsFunction;
import com.axellience.vuegwt.client.tools.VueGwtTools;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Function")
public class JsVueClass<T extends Vue> extends JsFunction
{
    @JsOverlay
    public final T instantiate()
    {
        return VueGwtTools.createInstanceForVueClass(this);
    }

    @JsOverlay
    public final <K extends T> JsVueClass<K> extend(VueComponentOptions<K> vueComponentOptions)
    {
        return VueGwtTools.extendVueClass(this, vueComponentOptions);
    }
}
