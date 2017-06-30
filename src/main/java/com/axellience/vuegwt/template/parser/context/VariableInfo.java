package com.axellience.vuegwt.template.parser.context;

/**
 * @author Adrien Baron
 */
public class VariableInfo
{
    private String type;
    private String name;

    public VariableInfo(String type, String name)
    {
        this.type = type;
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }
}
