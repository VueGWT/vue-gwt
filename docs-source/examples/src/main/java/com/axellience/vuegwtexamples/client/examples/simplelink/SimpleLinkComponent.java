package com.axellience.vuegwtexamples.client.examples.simplelink;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import jsinterop.annotations.JsProperty;

@Component
public class SimpleLinkComponent extends VueComponent
{
    @JsProperty String linkName = "Hello Vue GWT!";
}