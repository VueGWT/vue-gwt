package com.axellience.vuegwtexamples.client.examples.extendjavacomponent;

import com.axellience.vuegwt.client.component.hooks.HasCreated;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.Computed;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class ChildComponent extends ParentComponent implements HasCreated
{
    @JsProperty int childValue;

    @Override
    public void created()
    {
        this.childValue = 10;
    }

    @JsMethod
    public int childMultiplyBy10(int value)
    {
        return value * 10;
    }

    @JsMethod
    public int childMultiplyBy4(int value)
    {
        return this.parentMultiplyBy2(this.parentMultiplyBy2(value));
    }

    @Computed
    public String getChildComputed()
    {
        return "Child Computed | " + this.childValue;
    }
}
