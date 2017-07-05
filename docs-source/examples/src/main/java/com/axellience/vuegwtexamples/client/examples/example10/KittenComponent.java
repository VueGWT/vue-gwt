package com.axellience.vuegwtexamples.client.examples.example10;

import com.axellience.vuegwt.client.VueComponent;
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

    @Override
    public void created() {
        myKittenBundle = KittenClientBundle.INSTANCE;
    }
}