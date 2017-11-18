package com.axellience.vuegwtexamples.client.examples.greet;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import com.google.gwt.user.client.Window;
import jsinterop.annotations.JsMethod;

/**
 * @author Adrien Baron
 */
@Component
public class GreetComponent extends VueComponent
{
    @JsMethod
    public void greet()
    {
        Window.alert("Hello from GWT!");
    }
}
