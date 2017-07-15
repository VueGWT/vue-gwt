package com.axellience.vuegwtexamples.client.examples.buttonplusone;

import com.axellience.vuegwt.client.Vue;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
@Component
public class ButtonPlusOneComponent extends Vue
{
    public int counter;

    @Override
    public void created()
    {
        this.counter = 0;
    }
}
