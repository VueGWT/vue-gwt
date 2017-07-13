package com.axellience.vuegwt.client.jsnative;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.VueDirective;
import com.axellience.vuegwt.client.jsnative.types.JsObject;
import com.axellience.vuegwt.client.options.VueComponentOptions;
import com.axellience.vuegwt.client.options.VueDirectiveOptions;
import com.axellience.vuegwt.client.options.VueOptionsCache;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Adrien Baron
 */
@JsType(name = "VueGwt", namespace = JsPackage.GLOBAL)
public class VueGwt
{
    /**
     * Return the Component Options for a given Java {@link VueComponent}.
     * This object can be passed directly to new Vue() or to Vue.component().
     * @param componentClassCanonicalName The canonical name (full name) of the {@link VueComponent} we want the Options of.
     * @return The {@link VueComponentOptions} for the requested {@link VueComponent}
     */
    public static VueComponentOptions getComponentOptions(String componentClassCanonicalName)
    {
        return VueOptionsCache.getComponentOptions(componentClassCanonicalName);
    }

    /**
     * Return the Directive Options for a given Java {@link VueDirective}.
     * This object can be passed directly to Vue.directive().
     * @param directiveClassCanonicalName The canonical name (full name) of the {@link VueDirective} we want the Options of.
     * @return The {@link VueDirectiveOptions} for the requested {@link VueDirective}
     */
    public static VueDirectiveOptions getDirectiveOptions(String directiveClassCanonicalName)
    {
        return VueOptionsCache.getDirectiveOptions(directiveClassCanonicalName);
    }

    /**
     * Return an object with all the {@link VueComponentOptions} defined in the app.
     * @return An object with all the {@link VueComponentOptions} defined in the app
     */
    public static JsObject getComponentsOptions()
    {
        Map<String, VueComponentOptions> map = VueOptionsCache.getComponentOptionsCache();
        JsObject result = new JsObject();
        for (Entry<String, VueComponentOptions> entry : map.entrySet())
            result.set(entry.getKey(), entry.getValue());

        return result;
    }

    /**
     * Return an object with all the {@link VueDirectiveOptions} defined in the app.
     * @return An object with all the {@link VueDirectiveOptions} defined in the app
     */
    public static JsObject getDirectivesOptions()
    {
        Map<String, VueDirectiveOptions> map = VueOptionsCache.getDirectiveOptionsCache();
        JsObject result = new JsObject();
        for (Entry<String, VueDirectiveOptions> entry : map.entrySet())
            result.set(entry.getKey(), entry.getValue());

        return result;
    }
}
