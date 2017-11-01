package com.axellience.vuegwtexamples.client.examples.link;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import jsinterop.annotations.JsProperty;

@Component
public class LinkComponent extends VueComponent
{
    @JsProperty String linkName = "Hello Vue GWT!";
    @JsProperty String linkTarget = "https://github.com/Axellience/vue-gwt";
}