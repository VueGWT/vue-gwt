package com.axellience.vuegwt.client.options.component;

import jsinterop.annotations.JsProperty;

/**
 * Abstract class to link a Java and a Js name for a given property
 * @author Adrien Baron
 */
public abstract class NamedElementDefinition
{
    @JsProperty
    public final String javaName;
    @JsProperty
    public final String jsName;

    public NamedElementDefinition(String javaName)
    {
        this.javaName = javaName;
        this.jsName = javaName;
    }

    public NamedElementDefinition(String javaName, String jsName)
    {
        this.javaName = javaName;
        this.jsName = jsName;
    }
}
