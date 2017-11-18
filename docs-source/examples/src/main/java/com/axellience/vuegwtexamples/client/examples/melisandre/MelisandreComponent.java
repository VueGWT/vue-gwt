package com.axellience.vuegwtexamples.client.examples.melisandre;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class MelisandreComponent extends VueComponent
{
    @JsProperty boolean isRed = true;
}