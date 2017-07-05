package com.axellience.vuegwtexamples.client.examples.example1;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsType;

@JsType
@Component
public class SimpleLinkComponent extends VueComponent {
    public String linkName;

    @Override
    public void created() {
        this.linkName = "Hello Vue GWT!";
    }
}