package com.axellience.vuegwt.client.definitions;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.VueDirective;

import java.util.HashMap;
import java.util.Map;

/**
 * A Cache for generated Vue Definitions.
 * Using static initializer block, Vue Definitions register an instance of themselves in
 * this Cache.
 * VueGWT then use this cache to get definitions for VueComponents and VueDirectives.
 * @author Adrien Baron
 */
public class VueDefinitionCache
{
    private static Map<Class<? extends VueComponent>, VueComponentDefinition>
        componentDefinitionsCache = new HashMap<>();

    private static Map<Class<? extends VueDirective>, VueDirectiveDefinition>
        directiveDefinitionsCache = new HashMap<>();

    /**
     * Register a component in the cache, called by VueComponentDefinitions static block
     * @param vueComponentClass The class of the VueComponent to store the VueComponentDefinition
     * for
     * @param componentDefinition The VueComponentDefinition instance
     */
    public static <T extends VueComponent> void registerComponent(Class<T> vueComponentClass,
        VueComponentDefinition<T> componentDefinition)
    {
        componentDefinitionsCache.put(vueComponentClass, componentDefinition);
    }

    /**
     * Return the Definition for a given VueComponent
     * @param vueComponentClass The class of the VueComponent we want the VueComponentDefinition
     * from
     */
    public static <T extends VueComponent> VueComponentDefinition<T> getComponentDefinitionForClass(
        Class<T> vueComponentClass)
    {
        VueComponentDefinition<T> componentDefinition =
            componentDefinitionsCache.get(vueComponentClass);

        if (componentDefinition != null)
        {
            componentDefinition.ensureDependenciesInjected();
            return componentDefinition;
        }

        throw new RuntimeException("Couldn't find the given Component "
            + vueComponentClass.getCanonicalName()
            + ". Make sure your annotations are being processed, and that you added the -generateJsInteropExports flag to GWT.");
    }

    /**
     * Register a directive in the cache, called by VueDirectiveDefinitions static block
     * @param vueDirectiveClass The class of the VueDirective to store the VueDirectiveDefinition
     * for
     * @param directiveDefinition The VueDirectiveDefinition instance
     */
    public static void registerDirective(Class<? extends VueDirective> vueDirectiveClass,
        VueDirectiveDefinition directiveDefinition)
    {
        directiveDefinitionsCache.put(vueDirectiveClass, directiveDefinition);
    }

    /**
     * Return the Definition for a given VueComponent
     * @param vueDirectiveClass The class of the VueComponent we want the VueComponentDefinition
     * from
     */
    public static VueDirectiveDefinition getDirectiveDefinitionForClass(
        Class<? extends VueDirective> vueDirectiveClass)
    {
        VueDirectiveDefinition directiveDefinition =
            directiveDefinitionsCache.get(vueDirectiveClass);

        if (directiveDefinition != null)
        {
            return directiveDefinition;
        }

        throw new RuntimeException("Couldn't find the given Directive "
            + vueDirectiveClass.getCanonicalName()
            + ". Make sure your annotations are being processed, and that you added the -generateJsInteropExports flag to GWT.");
    }
}
