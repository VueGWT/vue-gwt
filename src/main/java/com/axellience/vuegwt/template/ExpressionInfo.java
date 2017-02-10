package com.axellience.vuegwt.template;

import com.google.gwt.core.ext.typeinfo.JType;

/**
 * @author Adrien Baron
 */
public class ExpressionInfo
{
    private String expression;
    private JType  type;

    public ExpressionInfo(String expression, JType type)
    {
        this.expression = expression;
        this.type = type;
    }

    public String getExpression()
    {
        return expression;
    }

    public JType getType()
    {
        return type;
    }
}
