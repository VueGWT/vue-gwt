package com.axellience.vuegwt.processors.component.template.parser.jericho;

import com.axellience.vuegwt.processors.component.template.parser.TemplateParserLogger;
import net.htmlparser.jericho.Logger;
import net.htmlparser.jericho.LoggerProvider;

public class TemplateParserLoggerProvider implements LoggerProvider
{
    private final TemplateParserLogger logger;

    public TemplateParserLoggerProvider(TemplateParserLogger logger)
    {
        this.logger = logger;
    }

    @Override
    public Logger getLogger(String name)
    {
        return logger;
    }

    @Override
    public Logger getSourceLogger()
    {
        return logger;
    }
}
