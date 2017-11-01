package com.axellience.vuegwtexamples.client.examples.hiwhat;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import com.google.gwt.user.client.Window;
import jsinterop.annotations.JsMethod;

/**
 * @author Adrien Baron
 */
@Component
public class HiWhatComponent extends VueComponent
{
    @JsMethod
    public void say(String message) {
        Window.alert(message);
    }
}
