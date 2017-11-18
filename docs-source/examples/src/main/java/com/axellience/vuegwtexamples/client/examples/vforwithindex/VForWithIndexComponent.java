package com.axellience.vuegwtexamples.client.examples.vforwithindex;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwtexamples.client.examples.common.Todo;
import elemental2.core.Array;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class VForWithIndexComponent extends VueComponent implements HasCreated
{
    @JsProperty String parentMessage = "Message from parent";
    @JsProperty Array<Todo> todos = new Array<>();

    @Override
    public void created()
    {
        this.todos.push(new Todo("Learn Java"));
        this.todos.push(new Todo("Learn Vue GWT"));
        this.todos.push(new Todo("Build something awesome"));
    }
}
