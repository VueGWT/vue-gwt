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
    @JsProperty JsArray<Todo> todos;
    @JsProperty String parentMessage;

    public VForWithIndexComponent() {
        this.parentMessage = "Message from parent";

        this.todos = new JsArray<>();
        this.todos.push(new Todo("Learn Java"));
        this.todos.push(new Todo("Learn Vue GWT"));
        this.todos.push(new Todo("Build something awesome"));
    }
}
