package com.axellience.vuegwt.client.jsnative.definitions.component;

import jsinterop.annotations.JsProperty;

public class NamedElementDefinition
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
