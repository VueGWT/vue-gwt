package com.axellience.vuegwtexamples.client.examples.exclamation;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

@Component
public class ExclamationComponent extends VueComponent
{
    @JsProperty String message = "Hello Vue GWT!";

    @JsMethod // This is added so the example can be interacted with in documentation
    public void addExclamationMark()
    {
        this.message += "!";
    }
}