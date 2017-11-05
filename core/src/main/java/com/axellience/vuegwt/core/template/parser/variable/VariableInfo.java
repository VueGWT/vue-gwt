package com.axellience.vuegwt.core.template.parser.variable;

import com.squareup.javapoet.TypeName;

import static com.axellience.vuegwt.core.generation.GenerationUtil.stringTypeToTypeName;

/**
 * Information about a Variable in the template.
 * Can be for example a variable accessible from Java (@JsProperty), a Computed Property a loop
 * variable (v-for) or method parameters.
 * @author Adrien Baron
 */
public class VariableInfo
{
    private TypeName type;
    private String name;

    public VariableInfo(String type, String name)
    {
        this.type = stringTypeToTypeName(type);
        this.name = name;
    }

    public TypeName getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }
}
