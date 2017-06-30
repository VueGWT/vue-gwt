package com.axellience.vuegwt.template.parser.context;

/**
 * @author Adrien Baron
 */
public class VariableInfo
{
    private String type;
    private String templateName;

    public VariableInfo(String type, String templateName)
    {
        this.type = type;
        this.templateName = templateName;
    }

    public String getType()
    {
        return type;
    }

    public String getTemplateName()
    {
        return templateName;
    }
}
