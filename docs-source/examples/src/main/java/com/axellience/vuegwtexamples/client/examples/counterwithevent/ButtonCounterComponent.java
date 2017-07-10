package com.axellience.vuegwtexamples.client.examples.counterwithevent;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
@Component
public class ButtonCounterComponent extends VueComponent
{
    public int counter;

    @Override
    public void created() {
        this.counter = 0;
    }

    public void increment() {
        this.counter++;
        this.$emit("increment");
    }
}
