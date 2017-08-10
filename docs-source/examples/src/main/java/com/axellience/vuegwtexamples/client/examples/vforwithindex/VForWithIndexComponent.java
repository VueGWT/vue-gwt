package com.axellience.vuegwtexamples.client.examples.vforwithindex;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.jsnative.jstypes.JsArray;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwtexamples.client.examples.common.Todo;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class VForWithIndexComponent extends VueComponent
{
    @JsProperty String parentMessage = "Message from parent";
    @JsProperty JsArray<Todo> todos = new JsArray<>();

    public VForWithIndexComponent()
    {
        this.todos.push(new Todo("Learn Java"));
        this.todos.push(new Todo("Learn Vue GWT"));
        this.todos.push(new Todo("Build something awesome"));
    }
}
