package com.axellience.vuegwtexamples.client.examples.message;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import jsinterop.annotations.JsProperty;

@Component
public class MessageComponent extends VueComponent
{
    @JsProperty String message = "Hello VueComponent GWT!";
}