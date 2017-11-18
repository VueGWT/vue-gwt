package com.axellience.vuegwtexamples.client.examples.counterwithevent;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component(components = ButtonCounterComponent.class)
public class CounterWithEventComponent extends VueComponent
{
    @JsProperty
    protected int total = 0;

    @JsMethod
    protected void incrementTotal() {
        this.total++;
    }
}
