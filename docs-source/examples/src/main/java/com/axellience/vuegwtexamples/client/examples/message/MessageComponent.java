package com.axellience.vuegwtexamples.client.examples.message;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsProperty;

@Component
public class MessageComponent extends VueComponent
{
    @JsProperty String message = "Hello VueComponent GWT!";
}