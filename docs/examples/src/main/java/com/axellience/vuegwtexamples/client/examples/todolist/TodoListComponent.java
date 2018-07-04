package com.axellience.vuegwtexamples.client.examples.todolist;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import com.axellience.vuegwtexamples.client.examples.common.Todo;
import java.util.LinkedList;
import java.util.List;
import jsinterop.annotations.JsProperty;

@Component(components = TodoComponent.class)
public class TodoListComponent implements IsVueComponent, HasCreated {

  @Data
  @JsProperty
  List<Todo> todos = new LinkedList<>();

  @Override
  public void created() {
    this.todos.add(new Todo("Learn Java"));
    this.todos.add(new Todo("Learn Vue GWT"));
    this.todos.add(new Todo("Build something awesome"));
  }
}