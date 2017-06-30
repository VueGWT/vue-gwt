package com.axellience.vuegwt.template.parser.context;

/**
 * @author Adrien Baron
 */
public class LocalVariableInfo extends VariableInfo
{
    private String javaName;

    public LocalVariableInfo(String typeQualifiedName, String templateName, String javaName)
    {
        super(typeQualifiedName, templateName);
        this.javaName = javaName;
    }

    public String getJavaName()
    {
        return javaName;
    }
}
