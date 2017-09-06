package com.axellience.vuegwtexamples.client.examples.extendsjscomponent;

import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.Computed;
import com.axellience.vuegwtexamples.client.examples.extendsjscomponent.parent.ParentJsComponent;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class ChildJavaComponent extends ParentJsComponent
{
    @JsProperty int childValue = 10;

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
