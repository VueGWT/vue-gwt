package com.axellience.vuegwtexamples.client.examples.reverse;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Computed;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class ReverseComponent implements IsVueComponent
{
    @JsProperty String message = "Hello";

    @Computed
    public String getReversedMessage()
    {
        return new StringBuilder(message).reverse().toString();
    }
}