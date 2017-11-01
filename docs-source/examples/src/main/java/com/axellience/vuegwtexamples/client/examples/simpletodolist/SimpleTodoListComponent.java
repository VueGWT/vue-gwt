package com.axellience.vuegwtexamples.client.examples.simpletodolist;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwtexamples.client.examples.common.Todo;
import jsinterop.annotations.JsProperty;

import java.util.LinkedList;
import java.util.List;

@Component
public class SimpleTodoListComponent extends VueComponent implements HasCreated
{
    @JsProperty List<Todo> todos = new LinkedList<>();

    @Override
    public void created()
    {
        this.todos.add(new Todo("Learn Java"));
        this.todos.add(new Todo("Learn Vue GWT"));
        this.todos.add(new Todo("Build something awesome"));
    }
}