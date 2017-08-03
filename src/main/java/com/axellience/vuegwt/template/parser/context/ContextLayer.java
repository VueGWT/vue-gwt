package com.axellience.vuegwt.template.parser.context;

import com.axellience.vuegwt.template.parser.variable.LocalVariableInfo;
import com.axellience.vuegwt.template.parser.variable.VariableInfo;
import com.google.gwt.core.ext.typeinfo.JField;

import java.util.HashMap;
import java.util.Map;

/**
 * A context layer holding local variables for a v-for.
 * @author Adrien Baron
 */
public class ContextLayer
{
    private final Map<String, VariableInfo> variables = new HashMap<>();

    private <T extends VariableInfo> T addVariable(T variableInfo)
    {
        variables.put(variableInfo.getName(), variableInfo);
        return variableInfo;
    }

    VariableInfo addVariable(String type, String name)
    {
        return addVariable(new VariableInfo(type, name));
    }

    LocalVariableInfo addLocalVariable(String type, String templateName)
    {
        return addVariable(new LocalVariableInfo(type, templateName));
    }

    VariableInfo addVariable(JField jField)
    {
        return addVariable(jField.getType().getQualifiedSourceName(), jField.getName());
    }

    VariableInfo addVariable(Class type, String name)
    {
        return addVariable(type.getCanonicalName(), name);
    }

    VariableInfo getVariableInfo(String name)
    {
        return variables.get(name);
    }
}