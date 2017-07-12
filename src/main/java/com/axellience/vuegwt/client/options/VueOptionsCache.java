package com.axellience.vuegwt.client.options;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.VueDirective;

import java.util.HashMap;
import java.util.Map;

/**
 * A Cache for generated Vue Options.
 * Using static initializer block, Vue Options register an instance of themselves in
 * this Cache.
 * VueGWT then use this cache to get options for VueComponents and VueDirectives.
 * @author Adrien Baron
 */
public class VueOptionsCache
{
    private static Map<Class<? extends VueComponent>, VueComponentOptions> componentOptionsCache =
        new HashMap<>();

    private static Map<Class<? extends VueDirective>, VueDirectiveOptions> directiveOptionsCache =
        new HashMap<>();

    /**
     * Register a component in the cache, called by VueComponentOptions static block
     * @param vueComponentClass The class of the VueComponent to store the VueComponentOptions
     * for
     * @param componentOptions The VueComponentOptions instance
     */
    public static <T extends VueComponent> void registerComponentOptions(Class<T> vueComponentClass,
        VueComponentOptions<T> componentOptions)
    {
        componentOptionsCache.put(vueComponentClass, componentOptions);
    }

    /**
     * Return the VueComponentOptions for a given VueComponent
     * @param vueComponentClass The class of the VueComponent we want the VueComponentOptions
     * from
     */
    public static <T extends VueComponent> VueComponentOptions<T> getComponentOptions(
        Class<T> vueComponentClass)
    {
        VueComponentOptions<T> componentOptions = componentOptionsCache.get(vueComponentClass);

        if (componentOptions != null)
        {
            componentOptions.ensureDependenciesInjected();
            return componentOptions;
        }

        throw new RuntimeException("Couldn't find the given Component "
            + vueComponentClass.getCanonicalName()
            + ". Make sure your annotations are being processed, and that you added the -generateJsInteropExports flag to GWT.");
    }

    /**
     * Register a directive in the cache, called by VueDirectiveOptions static block
     * @param vueDirectiveClass The class of the VueDirective to store the VueDirectiveOptions
     * for
     * @param directiveOptions The VueDirectiveOptions instance
     */
    public static void registerDirectiveOptions(Class<? extends VueDirective> vueDirectiveClass,
        VueDirectiveOptions directiveOptions)
    {
        directiveOptionsCache.put(vueDirectiveClass, directiveOptions);
    }

    /**
     * Return the VueDirectiveOptions for a given VueDirective
     * @param vueDirectiveClass The class of the VueDirective we want the VueDirectiveOptions
     * from
     */
    public static VueDirectiveOptions getDirectiveOptions(
        Class<? extends VueDirective> vueDirectiveClass)
    {
        VueDirectiveOptions directiveOptions = directiveOptionsCache.get(vueDirectiveClass);

        if (directiveOptions != null)
        {
            return directiveOptions;
        }

        throw new RuntimeException("Couldn't find the given Directive "
            + vueDirectiveClass.getCanonicalName()
            + ". Make sure your annotations are being processed, and that you added the -generateJsInteropExports flag to GWT.");
    }
}
