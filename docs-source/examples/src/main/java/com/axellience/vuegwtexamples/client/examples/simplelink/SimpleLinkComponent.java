package com.axellience.vuegwtexamples.client.examples.simplelink;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsProperty;

@Component
public class SimpleLinkComponent extends VueComponent
{
    @JsProperty String linkName = "Hello Vue GWT!";
}