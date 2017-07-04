package com.axellience.vuegwt.template.parser.context;

import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Adrien Baron
 */
public class ContextLayer
{
    private final Map<String, VariableInfo> variables = new HashMap<>();
    private final String contextPrefix;

    public ContextLayer(String contextPrefix)
    {
        this.contextPrefix = contextPrefix;
    }

    private VariableInfo addVariable(VariableInfo variableInfo)
    {
        variables.put(variableInfo.getName(), variableInfo);
        return variableInfo;
    }

    public VariableInfo addVariable(JField jField)
    {
        return addVariable(new VariableInfo(jField.getType().getQualifiedSourceName(),
            jField.getName()));
    }

    public VariableInfo addVariable(Class type, String name)
    {
        return addVariable(new VariableInfo(type.getCanonicalName(), name));
    }

    public VariableInfo addVariable(String type, String name)
    {
        return addVariable(new VariableInfo(type, name));
    }

    public VariableInfo addComputedVariable(JType type, String name)
    {
        return addVariable(new ComputedVariableInfo(type.getQualifiedSourceName(), name));
    }

    public LocalVariableInfo addLocalVariable(String type, String templateName)
    {
        return (LocalVariableInfo) addVariable(new LocalVariableInfo(type,
            templateName,
            contextPrefix + templateName));
    }

    public Collection<VariableInfo> getVariables()
    {
        return variables.values();
    }

    public VariableInfo getVariableInfo(String name)
    {
        return variables.get(name);
    }

    public boolean hasVariable(String name)
    {
        return variables.containsKey(name);
    }
}