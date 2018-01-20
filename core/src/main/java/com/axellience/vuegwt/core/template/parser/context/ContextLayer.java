package com.axellience.vuegwt.core.template.parser.context;

import com.axellience.vuegwt.core.template.parser.variable.LocalVariableInfo;
import com.axellience.vuegwt.core.template.parser.variable.VariableInfo;
import com.squareup.javapoet.TypeName;

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

    VariableInfo addVariable(TypeName type, String name)
    {
        return addVariable(new VariableInfo(type, name));
    }

    LocalVariableInfo addLocalVariable(String type, String templateName)
    {
        return addVariable(new LocalVariableInfo(type, templateName));
    }

    VariableInfo addVariable(Class type, String name)
    {
        return addVariable(type.getCanonicalName(), name);
    }

    VariableInfo getVariableInfo(String name)
    {
        return variables.get(name);
    }

    void addMethod(String methodName)
    {
        this.methods.add(methodName);
    }

    boolean hasMethod(String methodName)
    {
        return this.methods.contains(methodName);
    }
}