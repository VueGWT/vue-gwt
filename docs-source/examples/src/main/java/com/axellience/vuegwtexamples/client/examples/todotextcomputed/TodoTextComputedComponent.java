package com.axellience.vuegwtexamples.client.examples.todotextcomputed;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.Computed;
import com.axellience.vuegwtexamples.client.examples.common.Todo;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
@Component
public class TodoTextComputedComponent extends VueComponent
{
    public Todo todo;

    @Override
    public void created()
    {
        this.todo = new Todo("Hello World!");
    }

    @Computed
    public String getTodoText()
    {
        return this.todo.getText();
    }

    @Computed
    public void setTodoText(String text)
    {
        this.todo.setText(text);
    }
}