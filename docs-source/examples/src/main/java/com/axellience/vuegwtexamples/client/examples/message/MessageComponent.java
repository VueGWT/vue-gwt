package com.axellience.vuegwtexamples.client.examples.message;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsType;

@JsType
@Component
public class MessageComponent extends VueComponent {
    public String message;

    @Override
    public void created() {
        this.message = "Hello Vue GWT!";
    }
}