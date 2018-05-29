package com.axellience.vuegwtexamples.client.examples.vonwithdomevent;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import com.google.gwt.user.client.Window;
import elemental2.dom.Event;
import jsinterop.annotations.JsMethod;

/**
 * @author Adrien Baron
 */
@Component
public class VOnWithDOMEventComponent implements IsVueComponent
{
    @JsMethod
    public void warn(String message, Event event)
    {
        if (event != null)
        {
            event.preventDefault();
            message += " -> Event Target: " + event.target;
        }

        Window.alert(message);
    }
}