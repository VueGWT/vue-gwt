package com.axellience.vuegwtexamples.client.examples.greet;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.google.gwt.user.client.Window;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
@Component
public class GreetComponent extends VueComponent
{
    @Override
    public void created() {}

    public void greet()
    {
        Window.alert("Hello from GWT!");
    }
}
