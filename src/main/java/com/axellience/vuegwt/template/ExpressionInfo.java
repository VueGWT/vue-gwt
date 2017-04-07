package com.axellience.vuegwt.template;

/**
 * @author Adrien Baron
 */
public class ExpressionInfo
{
    private       String expression;
    private final String expressionType;

    public ExpressionInfo(String expression, String expressionType)
    {
        this.expression = expression;
        this.expressionType = expressionType;
    }

    public String getExpression()
    {
        return expression;
    }

    public String getExpressionType()
    {
        return expressionType;
    }
}
