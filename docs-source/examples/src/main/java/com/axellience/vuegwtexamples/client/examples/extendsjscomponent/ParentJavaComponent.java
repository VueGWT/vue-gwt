package com.axellience.vuegwtexamples.client.examples.extendsjscomponent;

import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class ParentJavaComponent extends ChildJsComponent
{
    @JsProperty
    int value;

    @Override
    public void created()
    {
        this.value = 2;
    }
}
