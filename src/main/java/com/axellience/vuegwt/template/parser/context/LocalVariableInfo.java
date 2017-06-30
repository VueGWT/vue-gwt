package com.axellience.vuegwt.template.parser.context;

/**
 * @author Adrien Baron
 */
public class LocalVariableInfo extends VariableInfo
{
    private String globalName;

    public LocalVariableInfo(String typeQualifiedName, String localName, String globalName)
    {
        super(typeQualifiedName, localName);
        this.globalName = globalName;
    }

    public String getGlobalName()
    {
        return globalName;
    }
}
