package com.axellience.vuegwtexamples.client.examples.message;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import jsinterop.annotations.JsProperty;

@Component
public class MessageComponent implements IsVueComponent
{
    @JsProperty String message = "Hello VueComponent GWT!";
}