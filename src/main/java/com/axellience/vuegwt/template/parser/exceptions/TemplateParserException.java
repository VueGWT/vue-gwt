package com.axellience.vuegwt.template.parser.exceptions;

import com.axellience.vuegwt.template.parser.context.TemplateParserContext;

/**
 * A generic exception thrown when parsing the template
 * @author Adrien Baron
 */
public abstract class TemplateParserException extends RuntimeException
{
    public TemplateParserException(String message, TemplateParserContext context)
    {
        this(message, context, null);
    }

    public TemplateParserException(String message, TemplateParserContext context, Throwable cause)
    {
        super("\nError: " + message + "\nIn Component: " + context
            .getComponentJsTypeClass()
            .getName() + "\nWhile processing Node: " + context.getCurrentNode().toString(), cause);
    }
}
