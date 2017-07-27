package com.axellience.vuegwtexamples.client.examples.canhide;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsType;

@JsType
@Component
public class CanHideComponent extends VueComponent
{
    public boolean visible;

    public CanHideComponent() {
        this.visible = true;
    }
}