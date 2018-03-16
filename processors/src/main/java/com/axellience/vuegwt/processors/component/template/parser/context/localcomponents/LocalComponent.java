package com.axellience.vuegwt.processors.component.template.parser.context.localcomponents;

import com.squareup.javapoet.TypeName;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.axellience.vuegwt.processors.utils.GeneratorsNameUtil.propNameToAttributeName;

public class LocalComponent
{
    private final Map<String, LocalComponentProp> attributeNameToPropMap;
    private final Map<String, LocalComponentProp> propNameToPropMap;
    private final Set<LocalComponentProp>         requiredProps;
    private final String                          componentTagName;

    LocalComponent(String componentTagName)
    {
        this.componentTagName = componentTagName;
        attributeNameToPropMap = new HashMap<>();
        propNameToPropMap = new HashMap<>();
        requiredProps = new HashSet<>();
    }

    public void addProp(String propName, TypeName propType, boolean isRequired)
    {
        String attributeName = propNameToAttributeName(propName);
        LocalComponentProp localComponentProp =
                new LocalComponentProp(propName, attributeName, propType, isRequired);
        attributeNameToPropMap.put(attributeName, localComponentProp);
        propNameToPropMap.put(propName, localComponentProp);

        if (isRequired)
            requiredProps.add(localComponentProp);
    }

    private Optional<LocalComponentProp> getProp(String attributeName)
    {
        if (attributeNameToPropMap.containsKey(attributeName))
            return Optional.of(attributeNameToPropMap.get(attributeName));

        if (propNameToPropMap.containsKey(attributeName))
            return Optional.of(propNameToPropMap.get(attributeName));

        return Optional.empty();
    }

    public Optional<LocalComponentProp> getPropForAttribute(String attributeName)
    {
        if (attributeName.toLowerCase().startsWith("v-bind:"))
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
