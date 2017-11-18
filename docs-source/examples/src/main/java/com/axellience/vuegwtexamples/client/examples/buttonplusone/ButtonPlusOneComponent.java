package com.axellience.vuegwtexamples.client.examples.buttonplusone;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class ButtonPlusOneComponent extends VueComponent
{
    @JsProperty int counter = 0;
}
