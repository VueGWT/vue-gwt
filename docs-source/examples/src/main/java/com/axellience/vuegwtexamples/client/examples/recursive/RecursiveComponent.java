package com.axellience.vuegwtexamples.client.examples.recursive;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.annotations.component.PropDefault;
import com.axellience.vuegwt.core.client.component.VueComponent;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component(name = "recursive")
public class RecursiveComponent extends VueComponent
{
    @Prop
    @JsProperty
    int counter;

    @PropDefault("counter")
    public int defaultCounter()
    {
        return 0;
    }
}
