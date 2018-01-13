package com.axellience.vuegwt.core.template.parser.context.localcomponents;

import com.squareup.javapoet.TypeName;

public class LocalComponentProp
{
    private String name;
    private TypeName type;
    boolean isRequired;

    LocalComponentProp(String name, TypeName type, boolean isRequired)
    {
        this.name = name;
        this.type = type;
        this.isRequired = isRequired;
    }

    public String getName()
    {
        return name;
    }

    public TypeName getType()
    {
        return type;
    }

    public boolean isRequired()
    {
        return isRequired;
    }
}
