package com.axellience.vuegwtexamples.client.examples.example5;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsType;

@JsType
@Component
public class Example5Component extends VueComponent {
    public String message;

    @Override
    public void created() {
        this.message = "Hello Vue GWT!";
    }

    public void addExclamationMark() {
        this.message += "!";
    }
}