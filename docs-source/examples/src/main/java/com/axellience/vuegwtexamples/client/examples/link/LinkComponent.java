package com.axellience.vuegwtexamples.client.examples.link;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsProperty;

@Component
public class LinkComponent extends VueComponent
{
    @JsProperty String linkTarget;
    @JsProperty String linkName;

    @Override
    public void created()
    {
        this.linkTarget = "https://github.com/Axellience/vue-gwt";
        this.linkName = "Hello Vue GWT!";
    }
}