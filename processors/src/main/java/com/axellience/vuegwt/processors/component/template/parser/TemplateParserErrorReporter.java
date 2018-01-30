package com.axellience.vuegwt.processors.component.template.parser;

import com.axellience.vuegwt.processors.component.template.parser.context.TemplateParserContext;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic.Kind;

public class TemplateParserErrorReporter
{
    private final TemplateParserContext context;
    private final Messager messager;

    TemplateParserErrorReporter(TemplateParserContext context, Messager messager)
    {
        this.context = context;
        this.messager = messager;
    }

    void reportError(String message, String expression)
    {
        reportError(message + "\n\nIn expression: " + expression);
    }

    void reportError(String message)
    {
        messager.printMessage(Kind.ERROR,
            "In "
                + context.getTemplateName()
                + ": "
                + message
                + "\nWhile processing Node: \n"
                + context.getCurrentElement().toString(),
            context.getComponentTypeElement());
    }
}
