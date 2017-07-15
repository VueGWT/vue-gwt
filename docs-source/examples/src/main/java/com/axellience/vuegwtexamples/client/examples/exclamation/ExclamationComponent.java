package com.axellience.vuegwtexamples.client.examples.exclamation;

import com.axellience.vuegwt.client.Vue;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsType;

@JsType
@Component
public class ExclamationComponent extends Vue
{
    public String message;

    @Override
    public void created() {
        this.message = "Hello Vue GWT!";
    }

    public void addExclamationMark() {
        this.message += "!";
    }
}