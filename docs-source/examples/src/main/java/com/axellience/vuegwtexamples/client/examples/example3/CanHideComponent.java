package com.axellience.vuegwtexamples.client.examples.example3;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsType;

@JsType
@Component
public class CanHideComponent extends VueComponent {
    public boolean visible;

    @Override
    public void created() {
        this.visible = true;
    }
}