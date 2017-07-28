package com.axellience.vuegwtexamples.client.examples.melisandre;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class MelisandreComponent extends VueComponent
{
    @JsProperty boolean isRed;

    public MelisandreComponent() {
        this.isRed = true;
    }
}