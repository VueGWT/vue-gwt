package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.jsnative.types.JsObject;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
public class VueDirectiveBinding
{
    public String name;
    public Object value;
    public Object oldValue;
    public String expression;
    public String arg;
    public JsObject modifiers;
}
