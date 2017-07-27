package com.axellience.vuegwtexamples.client.examples.bindinlinestyle;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
@Component
public class BindInlineStyleComponent extends VueComponent
{
    public String activeColor;
    public float fontSize;

    public BindInlineStyleComponent()
    {
        activeColor = "red";
        fontSize = 20;
    }
}
