package com.axellience.vuegwtexamples.client.examples.recursive;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.Prop;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component(name = "recursive")
public class RecursiveComponent extends VueComponent
{
    @Prop
    @JsProperty
    Integer counter;

    public RecursiveComponent()
    {
        if (this.counter == null)
            this.counter = 0;
    }
}
