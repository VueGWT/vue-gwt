package com.axellience.vuegwtexamples.client.examples.todotext;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.jsnative.JsTools;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwtexamples.client.examples.common.Todo;
import com.google.gwt.dom.client.NativeEvent;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
@Component
public class TodoTextComponent extends VueComponent
{
    public Todo todo;

    @Override
    public void created()
    {
        this.todo = new Todo("Hello World!");
    }

    public void updateTodoText(NativeEvent event)
    {
        this.todo.setText(JsTools.get(event.getEventTarget(), "value"));
    }
}