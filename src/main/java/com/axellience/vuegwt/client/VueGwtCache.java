package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.client.directive.VueDirective;
import com.axellience.vuegwt.client.directive.options.VueDirectiveOptions;
import com.axellience.vuegwt.client.vue.VueConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * A Cache for generated Vue Options.
 * Using static initializer block, Vue Options register an instance of themselves in
 * this Cache.
 * VueGWT then use this cache to get options for VueComponents and VueDirectives.
 * @author Adrien Baron
 */
public class VueGwtCache
{
    private static Map<String, VueConstructor<? extends Vue>> vueConstructorCache = new HashMap<>();
    private static Map<String, VueComponentOptions<? extends Vue>> componentOptionsCache =
        new HashMap<>();
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

    public static <T extends Vue> VueConstructor<T> getVueConstructor(Class<T> vueComponentClass)
    {
        return getVueConstructor(vueComponentClass.getCanonicalName());
    }

    public static <T extends Vue> VueConstructor<T> getVueConstructor(
        String vueComponentClassCanonicalName)
    {
        return (VueConstructor<T>) vueConstructorCache.computeIfAbsent(
            vueComponentClassCanonicalName,
            VueGwtCache::createVueConstructor);
    }

    private static <T extends Vue> VueConstructor<T> createVueConstructor(
        String vueComponentClassCanonicalName)
    {
        VueComponentOptions<T> componentOptions =
            (VueComponentOptions<T>) componentOptionsCache.get(vueComponentClassCanonicalName);

        if (componentOptions == null)
        {
            throw new RuntimeException("Couldn't find the given Component "
                + vueComponentClassCanonicalName
                + ". Make sure your annotations are being processed, and that you added the -generateJsInteropExports flag to GWT.");
        }

        VueConstructor<T> vueConstructor;
        Class<? super T> parent = componentOptions.getParentComponentClass();
        if (parent == null)
            vueConstructor = Vue.extend(componentOptions);
        else
            vueConstructor = getVueConstructor((Class<T>) parent).extend(componentOptions);

        vueConstructor.ensureDependenciesInjected(componentOptions.getLocalComponents(),
            componentOptions.getLocalDirectives());

        return vueConstructor;
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
     * Return the whole directive options cache map
     * @return The {@link VueDirectiveOptions} cache map
     */
    public static Map<String, VueDirectiveOptions> getDirectiveOptionsCache()
    {
        return directiveOptionsCache;
    }
}
