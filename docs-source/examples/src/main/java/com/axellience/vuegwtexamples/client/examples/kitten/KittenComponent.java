package com.axellience.vuegwtexamples.client.examples.kitten;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class KittenComponent extends VueComponent
{
    @JsProperty KittenClientBundle myKittenBundle;

    @Override
    public void created()
    {
        myKittenBundle = KittenClientBundle.INSTANCE;
    }
}