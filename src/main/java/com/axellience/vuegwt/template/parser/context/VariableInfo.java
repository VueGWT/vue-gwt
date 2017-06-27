package com.axellience.vuegwt.template.parser.context;

import com.google.gwt.core.ext.typeinfo.JType;

/**
 * @author Adrien Baron
 */
public class VariableInfo
{
    private JType   type;
    private String  templateName;
    private String  javaName;
    private boolean hasCustomJavaName;

    public VariableInfo(JType type, String templateName)
    {
        this(type, templateName, templateName);
    }

    public VariableInfo(JType type, String templateName, String javaName)
    {
        this.type = type;
        this.templateName = templateName;
        this.javaName = javaName;
        this.hasCustomJavaName = !templateName.equals(javaName);
    }

    public JType getType()
    {
        return type;
    }

    public String getTemplateName()
    {
        return templateName;
    }

    public String getJavaName()
    {
        return javaName;
    }

    public boolean hasCustomJavaName()
    {
        return hasCustomJavaName;
    }
}
