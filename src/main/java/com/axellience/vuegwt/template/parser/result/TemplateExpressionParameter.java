package com.axellience.vuegwt.template.parser.result;

/**
 * @author Adrien Baron
 */
public class TemplateExpressionParameter
{
    private final String type;
    private final String name;

    public TemplateExpressionParameter(String type, String name) {
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
