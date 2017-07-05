package com.axellience.vuegwtexamples.client.examples.example7;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsType;

@JsType
@Component(components = { TodoComponent.class })
public class ParentComponent extends VueComponent
{
    @Override
    public void created()
    {

    }
}