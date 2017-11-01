package com.axellience.vuegwt.gwt2.template.parser.context;

import com.axellience.vuegwt.gwt2.template.parser.variable.LocalVariableInfo;
import com.axellience.vuegwt.gwt2.template.parser.variable.VariableInfo;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JMethod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A context layer holding local variables for a v-for.
 * @author Adrien Baron
 */
public class ContextLayer
{
    private final Map<String, VariableInfo> variables = new HashMap<>();
    private final Set<String> methods = new HashSet<>();

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

    void addMethod(JMethod method)
    {
        this.methods.add(method.getName());
    }

    boolean hasMethod(String methodName)
    {
        return this.methods.contains(methodName);
    }
}