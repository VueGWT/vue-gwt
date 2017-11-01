package com.axellience.vuegwt.gwt2.template.parser.variable;

/**
 * Information about a Variable in the template.
 * Can be for example a variable accessible from Java (@JsProperty), a Computed Property a loop
 * variable (v-for) or method parameters.
 * @author Adrien Baron
 */
public class VariableInfo
{
    private String type;
    private String name;

    public VariableInfo(String type, String name)
    {
        this.type = type;
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }
}
