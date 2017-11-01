package com.axellience.vuegwtexamples.client.examples.recursive;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component(name = "recursive")
public class RecursiveComponent extends VueComponent implements HasCreated
{
    @Prop
    @JsProperty
    Integer counter;

    @Override
    public void created()
    {
        if (this.counter == null)
            this.counter = 0;
    }
}
