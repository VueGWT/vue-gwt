package com.axellience.vuegwt.client.options.component;

import jsinterop.annotations.JsProperty;

/**
 * Definition of a property
 * @author Adrien Baron
 */
public class PropDefinition
{
    @JsProperty public Object type;
    @JsProperty(name = "default") public Object defaultValue;
    @JsProperty public boolean required = false;
    @JsProperty public Object validator;
}
