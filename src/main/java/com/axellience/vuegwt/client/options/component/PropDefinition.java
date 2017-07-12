package com.axellience.vuegwt.client.options.component;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Definition of a property
 * @author Adrien Baron
 */
@JsType
public class PropDefinition
{
    public Object type;
    @JsProperty(name = "default")
    public Object defaultValue;
    public boolean required = false;
    public Object validator;
}
