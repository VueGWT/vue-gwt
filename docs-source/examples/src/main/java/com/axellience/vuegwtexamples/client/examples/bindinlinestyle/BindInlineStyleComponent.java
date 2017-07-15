package com.axellience.vuegwtexamples.client.examples.bindinlinestyle;

import com.axellience.vuegwt.client.Vue;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
@Component
public class BindInlineStyleComponent extends Vue
{
    public String activeColor;
    public float fontSize;

    @Override
    public void created()
    {
        activeColor = "red";
        fontSize = 20;
    }
}
