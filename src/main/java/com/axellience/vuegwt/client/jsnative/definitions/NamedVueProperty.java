package com.axellience.vuegwt.client.jsnative.definitions;

import jsinterop.annotations.JsType;

@JsType
public class NamedVueProperty
{
    protected String javaName;
    protected String jsName;

    public NamedVueProperty(String javaName, String jsName)
    {
        this.javaName = javaName;
        this.jsName = jsName;
    }

    public String getJavaName()
    {
        return javaName;
    }

    public String getJsName()
    {
        return jsName;
    }
}
