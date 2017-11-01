package com.axellience.vuegwtexamples.client.examples.counterwithevent;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class ButtonCounterComponent extends VueComponent
{
    @JsProperty
    protected int counter = 0;

    @JsMethod
    protected void increment() {
        this.counter++;
        this.$emit("increment");
    }
}
