package com.axellience.vuegwtexamples.client.examples.propdefaultvalue;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.annotations.component.PropDefault;
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
