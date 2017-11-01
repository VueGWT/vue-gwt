package com.axellience.vuegwtexamples.client.examples.extendjavacomponent;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Computed;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public abstract class ParentComponent extends VueComponent
{
    @JsProperty String parentMessage = "This is a message from the parent";

    @JsMethod
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
