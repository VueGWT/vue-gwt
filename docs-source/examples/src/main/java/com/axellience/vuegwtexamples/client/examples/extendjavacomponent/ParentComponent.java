package com.axellience.vuegwtexamples.client.examples.extendjavacomponent;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.Computed;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component(hasTemplate = false)
public class ParentComponent extends VueComponent
{
    @JsProperty String parentMessage;

    @Override
    public void created()
    {
        this.parentMessage = "This is a message from the parent";
    }

    public int parentMultiplyBy2(int value)
    {
        return value * 2;
    }

    @Computed
    public String getParentComputed()
    {
        return "Computed Message | " + this.parentMessage;
    }
}
