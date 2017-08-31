package com.axellience.vuegwtexamples.client.examples.propdefaultvalue;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.Prop;
import com.axellience.vuegwt.jsr69.component.annotations.PropDefault;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class PropDefaultValueComponent extends VueComponent
{
    @Prop
    @JsProperty
    String stringProp;

    @PropDefault(propertyName = "stringProp")
    String stringPropDefault()
    {
        return "Hello World";
    }
}
