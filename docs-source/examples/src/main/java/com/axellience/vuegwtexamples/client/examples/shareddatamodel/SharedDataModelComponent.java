package com.axellience.vuegwtexamples.client.examples.shareddatamodel;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component(useFactory = false)
public class SharedDataModelComponent extends VueComponent
{
    @JsProperty int counter = 0;
}
