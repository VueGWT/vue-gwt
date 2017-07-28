package com.axellience.vuegwtexamples.client.examples.buttonplusone;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class ButtonPlusOneComponent extends VueComponent
{
    @JsProperty int counter;

    public ButtonPlusOneComponent()
    {
        this.counter = 0;
    }
}
