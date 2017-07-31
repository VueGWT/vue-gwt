package com.axellience.vuegwtexamples.client.examples.todolist;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwtexamples.client.examples.common.Todo;
import jsinterop.annotations.JsProperty;

import java.util.LinkedList;
import java.util.List;

@Component(components = TodoComponent.class)
public class TodoListComponent extends VueComponent
{
    @JsProperty
    protected List<Todo> todos;

    @Override
    public void created()
    {
        this.todos = new LinkedList<>();
        this.todos.add(new Todo("Learn Java"));
        this.todos.add(new Todo("Learn Vue GWT"));
        this.todos.add(new Todo("Build something awesome"));
    }
}