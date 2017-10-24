package com.axellience.vuegwtexamples.client.examples.todotext;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.tools.JsUtils;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwtexamples.client.examples.common.Todo;
import com.google.gwt.dom.client.NativeEvent;
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
    public void updateTodoText(NativeEvent event)
    {
        this.todo.setText(JsUtils.get(event.getEventTarget(), "value"));
    }
}