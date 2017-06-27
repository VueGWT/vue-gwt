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
        variables.put(variableInfo.getTemplateName(), variableInfo);
        return variableInfo;
    }

    public VariableInfo addRootVariable(JField jField)
    {
        return addVariable(new VariableInfo(jField.getType(), jField.getName()));
    }

    public VariableInfo addLocalVariable(JType type, String templateName)
    {
        return addVariable(new VariableInfo(type, templateName, contextPrefix + templateName));
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