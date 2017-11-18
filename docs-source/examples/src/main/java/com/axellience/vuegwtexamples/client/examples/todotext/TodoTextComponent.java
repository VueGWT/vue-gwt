package com.axellience.vuegwtexamples.client.examples.todotext;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwtexamples.client.examples.common.Todo;
import elemental2.dom.Event;
import elemental2.dom.HTMLInputElement;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class TodoTextComponent extends VueComponent
{
    @JsProperty Todo todo = new Todo("Hello World!");

    @JsMethod
    public void updateTodoText(Event event)
    {
        this.todo.setText(((HTMLInputElement) event.target).value);
    }
}