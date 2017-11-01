package com.axellience.vuegwtexamples.client.examples.shareddatamodel;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component(useFactory = false)
public class SharedDataModelComponent extends VueComponent
{
    @JsProperty int counter = 0;
}
