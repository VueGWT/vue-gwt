package com.axellience.vuegwtexamples.client.examples.todolist;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
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
}