package com.axellience.vuegwtexamples.client.examples.reverse;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.Computed;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class ReverseComponent extends VueComponent
{
    @JsProperty String message = "Hello";

    @Computed
    public String getReversedMessage()
    {
        return new StringBuilder(message).reverse().toString();
    }
}