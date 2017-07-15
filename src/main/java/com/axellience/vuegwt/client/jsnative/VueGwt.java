package com.axellience.vuegwt.client.jsnative;

import com.axellience.vuegwt.client.VueGwtCache;
import com.axellience.vuegwt.client.directive.VueDirective;
import com.axellience.vuegwt.client.directive.options.VueDirectiveOptions;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
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
     * Return the Directive Options for a given Java {@link VueDirective}.
     * This object can be passed directly to Vue.directive().
     * @param directiveClassCanonicalName The canonical name (full name) of the {@link VueDirective} we want the Options of.
     * @return The {@link VueDirectiveOptions} for the requested {@link VueDirective}
     */
    public static VueDirectiveOptions getDirectiveOptions(String directiveClassCanonicalName)
    {
        return VueGwtCache.getDirectiveOptions(directiveClassCanonicalName);
    }

    /**
     * Return an object with all the {@link VueDirectiveOptions} defined in the app.
     * @return An object with all the {@link VueDirectiveOptions} defined in the app
     */
    public static JsObject<VueDirectiveOptions> getDirectivesOptions()
    {
        Map<String, VueDirectiveOptions> map = VueGwtCache.getDirectiveOptionsCache();
        JsObject<VueDirectiveOptions> result = new JsObject<>();
        for (Entry<String, VueDirectiveOptions> entry : map.entrySet())
            result.set(entry.getKey(), entry.getValue());

        return result;
    }
}
