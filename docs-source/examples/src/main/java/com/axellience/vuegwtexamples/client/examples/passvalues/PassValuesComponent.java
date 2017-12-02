package com.axellience.vuegwtexamples.client.examples.passvalues;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.client.component.VueComponent;
import jsinterop.annotations.JsProperty;

@Component
public class PassValuesComponent extends VueComponent
{
    @Prop
    @JsProperty
    int myInt;

    @Prop
    @JsProperty
    boolean myBool;

    @Prop
    @JsProperty
    double myDouble;

    @Prop
    @JsProperty
    float myFloat;

    @Prop
    @JsProperty
    long myLong;

    @Prop
    @JsProperty
    Integer wrappedInteger;

    @Prop
    @JsProperty
    Boolean wrappedBoolean;

    @Prop
    @JsProperty
    Double wrappedDouble;

    @Prop
    @JsProperty
    Float wrappedFloat;

    @Prop
    @JsProperty
    Long wrappedLong;
}
