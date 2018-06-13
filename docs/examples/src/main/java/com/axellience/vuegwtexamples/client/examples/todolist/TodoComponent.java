package com.axellience.vuegwtexamples.client.examples.todolist;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwtexamples.client.examples.common.Todo;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component(name = "my-todo")
public class TodoComponent implements IsVueComponent {

  @Prop
  @JsProperty
  public Todo todo;
}