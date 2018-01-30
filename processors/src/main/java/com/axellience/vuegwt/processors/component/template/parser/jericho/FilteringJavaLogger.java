package com.axellience.vuegwt.processors.component.template.parser.jericho;

import net.htmlparser.jericho.Logger;

public class FilteringJavaLogger implements Logger
{
    private final java.util.logging.Logger javaLogger;
    private final String templateName;

    public FilteringJavaLogger(final java.util.logging.Logger javaLogger, String templateName)
    {
        this.javaLogger = javaLogger;
        this.templateName = templateName;
    }

    public void error(final String message)
    {
        // Filter errors about invalid first attribute character
        // The @ sign is not recognized as valid when its valid in Vue GWT
        // A cleaner way would be to fork Jericho to add the support for @
        // But this filter should do for now.
        if (message.contains("contains attribute name with invalid first character"))
            return;

        javaLogger.severe(templateName + ": " + message);
    }

    public void warn(final String message)
    {
        javaLogger.warning(templateName + ": " + message);
    }

    public void info(final String message)
    {
        javaLogger.info(templateName + ": " + message);
    }

    public void debug(final String message)
    {
        javaLogger.fine(templateName + ": " + message);
    }

    public boolean isErrorEnabled()
    {
        return javaLogger.isLoggable(java.util.logging.Level.SEVERE);
    }

    public boolean isWarnEnabled()
    {
        return javaLogger.isLoggable(java.util.logging.Level.WARNING);
    }

    public boolean isInfoEnabled()
    {
        return javaLogger.isLoggable(java.util.logging.Level.INFO);
    }

    public boolean isDebugEnabled()
    {
        return javaLogger.isLoggable(java.util.logging.Level.FINE);
    }
}