package com.axellience.vuegwtexamples.client.examples.errorboundary;

import com.axellience.vuegwt.client.component.HasRender;
import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.hooks.HasCreated;
import com.axellience.vuegwt.client.vnode.VNode;
import com.axellience.vuegwt.client.vnode.builder.VNodeBuilder;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwtexamples.client.examples.common.Todo;

@Component(hasTemplate = false)
public class ErrorMakerComponent extends VueComponent implements HasCreated, HasRender
{
    @Override
    public void created()
    {
        Todo nullTodo = null;
        nullTodo.getText(); // This will break
    }

    @Override
    public VNode render(VNodeBuilder builder)
    {
        return builder.el("div");
    }
}
