package com.axellience.vuegwtexamples.client.examples.bindinlinestyle;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class BindInlineStyleComponent implements IsVueComponent
{
    @JsProperty String activeColor = "red";
    @JsProperty float fontSize = 20;
}
