package com.axellience.vuegwtexamples.client.examples.shareddatamodel;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
@Component(useFactory = false)
public class SharedDataModelComponent extends VueComponent
{
    public int counter;

    @Override
    public void created() {
        this.counter = 0;
    }
}
