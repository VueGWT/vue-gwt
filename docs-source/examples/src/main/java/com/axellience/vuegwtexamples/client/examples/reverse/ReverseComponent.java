package com.axellience.vuegwtexamples.client.examples.reverse;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.Computed;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
@Component
public class ReverseComponent extends VueComponent
{
    public String message;

    @Override
    public void created()
    {
        this.message = "Hello";
    }

    @Computed
    public String getReversedMessage()
    {
        return new StringBuilder(message).reverse().toString();
    }
}