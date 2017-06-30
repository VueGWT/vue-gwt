package com.axellience.vuegwt.template.parser.context;

import com.axellience.vuegwt.jsr69.component.annotations.Computed;

/**
 * @author Adrien Baron
 */
public class ComputedVariableInfo extends VariableInfo
{
    public ComputedVariableInfo(String type, String templateName)
    {
        super(type, templateName);
    }

    public String getGlobalName()
    {
        return this.getName() + Computed.COMPUTED_SUFFIX;
    }
}
