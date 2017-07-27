package com.axellience.vuegwtexamples.client.examples.extendjavacomponent;

import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.Computed;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
@Component
public class ChildComponent extends ParentComponent
{
    public int childValue;

    public ChildComponent()
    {
        this.childValue = 10;
    }

    public int childMultiplyBy10(int value)
    {
        return value * 10;
    }

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
