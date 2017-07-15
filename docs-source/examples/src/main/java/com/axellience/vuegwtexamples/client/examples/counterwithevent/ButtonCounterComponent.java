package com.axellience.vuegwtexamples.client.examples.counterwithevent;

import com.axellience.vuegwt.client.Vue;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class ButtonCounterComponent extends Vue
{
    @JsProperty
    protected int counter;

    @Override
    public void created() {
        this.counter = 0;
    }

    @JsMethod
    protected void increment() {
        this.counter++;
        this.$emit("increment");
    }
}
