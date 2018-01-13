package com.axellience.vuegwt.core.template.parser.context.localcomponents;

import com.squareup.javapoet.TypeName;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class LocalComponent
{
    private final Map<String, LocalComponentProp> propsMap;
    private final Set<LocalComponentProp> requiredProps;
    private final String componentTagName;

    LocalComponent(String componentTagName)
    {
        this.componentTagName = componentTagName;
        propsMap = new HashMap<>();
        requiredProps = new HashSet<>();
    }

    public void addProp(String propName, TypeName propType, boolean isRequired)
    {
        LocalComponentProp localComponentProp =
            new LocalComponentProp(propName, propType, isRequired);
        propsMap.put(propName, localComponentProp);

        if (isRequired)
            requiredProps.add(localComponentProp);
    }

    private Optional<LocalComponentProp> getProp(String propName)
    {
        if (!propsMap.containsKey(propName))
            return Optional.empty();

        return Optional.of(propsMap.get(propName));
    }

    public Optional<LocalComponentProp> getPropForAttribute(String attributeName)
    {
        if (attributeName.startsWith("v-bind:"))
            return getProp(attributeName.substring("v-bind:".length()));
        if (attributeName.startsWith(":"))
            return getProp(attributeName.substring(1));

        return Optional.empty();
    }

    public Set<LocalComponentProp> getRequiredProps()
    {
        return requiredProps;
    }

    public String getComponentTagName()
    {
        return componentTagName;
    }
}
