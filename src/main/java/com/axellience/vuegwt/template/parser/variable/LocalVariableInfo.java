package com.axellience.vuegwt.template.parser.variable;

/**
 * Information about a local variable (for example a v-for loop variable).
 * @author Adrien Baron
 */
public class LocalVariableInfo extends VariableInfo
{
    public LocalVariableInfo(String typeQualifiedName, String localName)
    {
        super(typeQualifiedName, localName);
    }
}
