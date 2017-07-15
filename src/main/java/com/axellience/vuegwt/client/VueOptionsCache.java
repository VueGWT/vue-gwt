package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.client.directive.VueDirective;
import com.axellience.vuegwt.client.directive.options.VueDirectiveOptions;

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
    private static Map<String, VueComponentOptions> componentOptionsCache = new HashMap<>();
    private static Map<String, VueDirectiveOptions> directiveOptionsCache = new HashMap<>();

    /**
     * Register a component in the cache, called by {@link VueComponentOptions} static block.
     * @param vueComponentClass The class of the {@link Vue} to store the {@link
     * VueComponentOptions}
     * for
     * @param componentOptions The options we want to register
     * @param <T> The {@link Vue} we want to register the options for
     */
    public static <T extends Vue> void registerComponentOptions(Class<T> vueComponentClass,
        VueComponentOptions<T> componentOptions)
    {
        componentOptionsCache.put(vueComponentClass.getCanonicalName(), componentOptions);
    }

    /**
     * Return the {@link VueComponentOptions} for a given {@link Vue}.
     * @param vueComponentClass The class of the {@link Vue} we want the
     * {@link VueComponentOptions} from
     * @param <T> The {@link Vue} we want to register the options for
     * @return The {@link VueComponentOptions} for the given {@link Vue}
     */
    public static <T extends Vue> VueComponentOptions<T> getComponentOptions(
        Class<T> vueComponentClass)
    {
        return getComponentOptions(vueComponentClass.getCanonicalName());
    }

    /**
     * Return the {@link VueComponentOptions} for a given {@link Vue}.
     * @param vueComponentClassCanonicalName The canonical name of the {@link Vue} class
     * we want the {@link VueComponentOptions} of
     * @param <T> The {@link Vue} we want to register the options for
     * @return The {@link VueComponentOptions} for the given {@link Vue}
     */
    public static <T extends Vue> VueComponentOptions<T> getComponentOptions(
        String vueComponentClassCanonicalName)
    {
        VueComponentOptions<T> componentOptions =
            componentOptionsCache.get(vueComponentClassCanonicalName);

        if (componentOptions != null)
        {
            componentOptions.ensureDependenciesInjected();
            return componentOptions;
        }

        throw new RuntimeException("Couldn't find the given Component "
            + vueComponentClassCanonicalName
            + ". Make sure your annotations are being processed, and that you added the -generateJsInteropExports flag to GWT.");
    }

    /**
     * Register a directive in the cache, called by VueDirectiveOptions static block.
     * @param vueDirectiveClass The class of the {@link VueDirective} to store the
     * {@link VueDirectiveOptions} for
     * @param directiveOptions The VueDirectiveOptions instance
     */
    public static void registerDirectiveOptions(Class<? extends VueDirective> vueDirectiveClass,
        VueDirectiveOptions directiveOptions)
    {
        directiveOptionsCache.put(vueDirectiveClass.getCanonicalName(), directiveOptions);
    }

    /**
     * Return the {@link VueDirectiveOptions} for a given {@link VueDirective}.
     * @param vueDirectiveClass The class of the {@link VueDirective} we want the {@link
     * VueDirectiveOptions}
     * from
     * @return The {@link VueDirectiveOptions} for the given {@link VueDirective}
     */
    public static VueDirectiveOptions getDirectiveOptions(
        Class<? extends VueDirective> vueDirectiveClass)
    {
        return getDirectiveOptions(vueDirectiveClass.getCanonicalName());
    }

    /**
     * Return the {@link VueDirectiveOptions} for a given {@link VueDirective}.
     * @param vueDirectiveClassCanonicalName The canonical name of the {@link VueDirective} class
     * we want the {@link VueDirectiveOptions} from
     * @return The {@link VueDirectiveOptions} for the given {@link VueDirective}
     */
    public static VueDirectiveOptions getDirectiveOptions(String vueDirectiveClassCanonicalName)
    {
        VueDirectiveOptions directiveOptions =
            directiveOptionsCache.get(vueDirectiveClassCanonicalName);

        if (directiveOptions != null)
        {
            return directiveOptions;
        }

        throw new RuntimeException("Couldn't find the given Directive "
            + vueDirectiveClassCanonicalName
            + ". Make sure your annotations are being processed, and that you added the -generateJsInteropExports flag to GWT.");
    }

    /**
     * Return the whole component options cache map
     * @return The {@link VueComponentOptions} cache map
     */
    public static Map<String, VueComponentOptions> getComponentOptionsCache()
    {
        return componentOptionsCache;
    }

    /**
     * Return the whole directive options cache map
     * @return The {@link VueDirectiveOptions} cache map
     */
    public static Map<String, VueDirectiveOptions> getDirectiveOptionsCache()
    {
        return directiveOptionsCache;
    }
}
