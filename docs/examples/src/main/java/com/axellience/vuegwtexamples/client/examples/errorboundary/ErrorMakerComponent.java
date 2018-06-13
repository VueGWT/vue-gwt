package com.axellience.vuegwtexamples.client.examples.errorboundary;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import com.axellience.vuegwt.core.client.component.hooks.HasRender;
import com.axellience.vuegwt.core.client.vnode.VNode;
import com.axellience.vuegwt.core.client.vnode.builder.VNodeBuilder;
import com.axellience.vuegwtexamples.client.examples.common.Todo;

@Component(hasTemplate = false)
public class ErrorMakerComponent implements IsVueComponent, HasCreated, HasRender {

  @Override
  public void created() {
    Todo nullTodo = null;
    nullTodo.getText(); // This will break
  }

  @Override
  public VNode render(VNodeBuilder builder) {
    return builder.el("div");
  }
}
