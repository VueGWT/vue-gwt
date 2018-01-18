package com.axellience.vuegwt.core.template.parser.context.localcomponents;

import com.squareup.javapoet.TypeName;

public class LocalComponentProp
{
    private String   propName;
    private String   attributeName;
    private TypeName type;
    boolean          isRequired;

    LocalComponentProp(String propName, String attributeName, TypeName type, boolean isRequired)
    {
        this.propName = propName;
        this.attributeName = attributeName;
        this.type = type;
        this.isRequired = isRequired;
    }

    public String getPropName()
    {
        return propName;
    }

    public String getAttributeName()
    {
        return attributeName;
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
