package com.axellience.vuegwtexamples.client.examples.canhide;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsProperty;

@Component
public class CanHideComponent extends VueComponent
{
    @JsProperty boolean visible = true;
}