package com.axellience.vuegwtexamples.client.examples.counterwithevent;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
@Component(components = ButtonCounterComponent.class)
public class CounterWithEventComponent extends VueComponent
{
    public int total;

    @Override
    public void created()
    {
        this.total = 0;
    }

    public void incrementTotal() {
        this.total++;
    }
}
