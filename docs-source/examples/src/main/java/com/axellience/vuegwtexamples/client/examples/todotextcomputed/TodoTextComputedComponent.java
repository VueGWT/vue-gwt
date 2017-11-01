package com.axellience.vuegwtexamples.client.examples.todotextcomputed;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwtexamples.client.examples.common.Todo;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class TodoTextComputedComponent extends VueComponent
{
    @JsProperty Todo todo = new Todo("Hello World!");

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