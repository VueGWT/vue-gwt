package com.axellience.vuegwtexamples.client.examples.hiwhat;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.google.gwt.user.client.Window;

/**
 * @author Adrien Baron
 */
@Component
public class HiWhatComponent extends VueComponent
{
    public void say(String message) {
        Window.alert(message);
    }
}
