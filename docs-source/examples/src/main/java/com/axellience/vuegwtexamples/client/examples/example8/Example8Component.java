package com.axellience.vuegwtexamples.client.examples.example8;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.jsnative.types.JsArray;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwtexamples.client.examples.common.Todo;
import jsinterop.annotations.JsType;

@JsType
@Component(components = { TodoComponent.class })
public class Example8Component extends VueComponent
{
    public JsArray<Todo> todos;

    @Override
    public void created()
    {
        this.todos = new JsArray<>();
        this.todos.push(new Todo("Learn Java"));
        this.todos.push(new Todo("Learn Vue GWT"));
        this.todos.push(new Todo("Build something awesome"));
    }
}