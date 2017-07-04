package com.axellience.vuegwtexamples.client.examples.example10;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
@Component
public class Example10Component extends VueComponent
{
    public Example10ClientBundle myBundle;

    @Override
    public void created() {
        myBundle = Example10ClientBundle.INSTANCE;
    }
}