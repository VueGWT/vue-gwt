package com.axellience.vuegwt.processors.component.template.parser.context.localcomponents;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.squareup.javapoet.TypeName;

import static com.axellience.vuegwt.core.generation.GenerationNameUtil.propNameToAttributeName;

public class LocalComponent
{
    private final Map<String, LocalComponentProp> attributeNameToPropMap;
    private final Set<LocalComponentProp>         requiredProps;
    private final String                          componentTagName;

    LocalComponent(String componentTagName)
    {
        this.componentTagName = componentTagName;
        attributeNameToPropMap = new HashMap<>();
        requiredProps = new HashSet<>();
    }

    public void addProp(String propName, TypeName propType, boolean isRequired)
    {
        String attributeName = propNameToAttributeName(propName);
        LocalComponentProp localComponentProp =
                new LocalComponentProp(propName, attributeName, propType, isRequired);
        attributeNameToPropMap.put(attributeName, localComponentProp);

        if (isRequired)
            requiredProps.add(localComponentProp);
    }

    private Optional<LocalComponentProp> getProp(String attributeName)
    {
        if (!attributeNameToPropMap.containsKey(attributeName))
            return Optional.empty();

        return Optional.of(attributeNameToPropMap.get(attributeName));
    }

    public Optional<LocalComponentProp> getPropForAttribute(String attributeName)
    {
        if (attributeName.startsWith("v-bind:"))
            return getProp(attributeName.substring("v-bind:".length()));
        if (attributeName.startsWith(":"))
            return getProp(attributeName.substring(1));

        return getProp(attributeName);
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
