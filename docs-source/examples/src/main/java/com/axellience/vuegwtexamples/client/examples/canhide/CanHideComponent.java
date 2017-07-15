package com.axellience.vuegwtexamples.client.examples.canhide;

import com.axellience.vuegwt.client.Vue;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsType;

@JsType
@Component
public class CanHideComponent extends Vue
{
    public boolean visible;

    @Override
    public void created() {
        this.visible = true;
    }
}