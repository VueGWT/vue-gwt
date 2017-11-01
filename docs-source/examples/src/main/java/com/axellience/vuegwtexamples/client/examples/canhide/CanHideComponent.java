package com.axellience.vuegwtexamples.client.examples.canhide;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import jsinterop.annotations.JsProperty;

@Component
public class CanHideComponent extends VueComponent
{
    @JsProperty boolean visible = true;
}