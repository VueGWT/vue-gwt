package com.axellience.vuegwtexamples.client.examples.kitten;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
@Component
public class KittenComponent extends VueComponent
{
    public KittenClientBundle myKittenBundle;

    public KittenComponent() {
        myKittenBundle = KittenClientBundle.INSTANCE;
    }
}