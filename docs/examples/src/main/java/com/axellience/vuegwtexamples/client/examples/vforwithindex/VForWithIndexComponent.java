package com.axellience.vuegwtexamples.client.examples.vforwithindex;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import com.axellience.vuegwtexamples.client.examples.common.Todo;
import elemental2.core.JsArray;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class VForWithIndexComponent implements IsVueComponent, HasCreated {

  @Data
  String parentMessage = "Message from parent";
  @Data
  JsArray<Todo> todos = new JsArray<>();

  @Override
  public void created() {
    this.todos.push(new Todo("Learn Java"));
    this.todos.push(new Todo("Learn Vue GWT"));
    this.todos.push(new Todo("Build something awesome"));
  }
}
