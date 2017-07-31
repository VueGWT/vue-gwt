package com.axellience.vuegwtexamples.client.examples.todolist;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.Prop;
import com.axellience.vuegwtexamples.client.examples.common.Todo;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class TodoComponent extends VueComponent
{
    @Prop
    @JsProperty
    public Todo todo;

    @Override
    public void created()
    {

    }
}