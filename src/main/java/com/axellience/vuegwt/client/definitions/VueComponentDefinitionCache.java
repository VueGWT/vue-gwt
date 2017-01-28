package com.axellience.vuegwt.client.definitions;

import com.axellience.vuegwt.client.VueComponent;

import java.util.HashMap;
import java.util.Map;

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
        VueComponentDefinition componentDefinition = componentDefinitionsCache.get(vueComponentClass);
        if (componentDefinition != null)
            return componentDefinition;

        throw new RuntimeException(
            "Couldn't find the given Component " + vueComponentClass.getCanonicalName() +
                ". Are you sure annotations are being processed?");
    }
}
