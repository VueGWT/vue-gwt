package com.axellience.vuegwt.client.definitions;

import com.axellience.vuegwt.client.VueComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * A Cache for generated VueComponentDefinitions.
 * Using static initializer block, VueComponentDefinitions register an instance of themselves in
 * this Cache.
 * VueGWT then use this cache to get definitions for VueComponents.
 * @author Adrien Baron
 */
public class VueComponentDefinitionCache
{
    private static Map<Class<? extends VueComponent>, VueComponentDefinition>
        componentDefinitionsCache = new HashMap<>();

    public static void registerComponent(Class<? extends VueComponent> vueComponentClass,
        VueComponentDefinition componentDefinition)
    {
        componentDefinitionsCache.put(vueComponentClass, componentDefinition);
    }

    public static VueComponentDefinition getComponentDefinitionForClass(
        Class<? extends VueComponent> vueComponentClass)
    {
        VueComponentDefinition componentDefinition =
            componentDefinitionsCache.get(vueComponentClass);

        if (componentDefinition != null) {
            componentDefinition.ensureChildComponentsInjected();
            return componentDefinition;
        }

        throw new RuntimeException("Couldn't find the given Component "
            + vueComponentClass.getCanonicalName()
            + ". Make sure your annotations are being processed, and that you added the -generateJsInteropExports flag to GWT.");
    }
}
