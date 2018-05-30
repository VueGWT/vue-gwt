package com.axellience.vuegwtexamples.client.examples.kitten;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class KittenComponent implements IsVueComponent
{
    @JsProperty KittenClientBundle myKittenBundle = KittenClientBundle.INSTANCE;
}