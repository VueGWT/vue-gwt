package com.axellience.vuegwtexamples.client.examples.simplelink;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsType;

@JsType
@Component
public class SimpleLinkComponent extends VueComponent
{
    public String linkName;

    public SimpleLinkComponent()
    {
        this.linkName = "Hello Vue GWT!";
    }
}