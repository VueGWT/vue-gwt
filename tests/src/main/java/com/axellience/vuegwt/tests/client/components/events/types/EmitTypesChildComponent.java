package com.axellience.vuegwt.tests.client.components.events.types;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import com.axellience.vuegwt.tests.client.common.Todo;

@Component(hasTemplate = false)
public class EmitTypesChildComponent extends VueComponent implements HasCreated
{
    @Override
    public void created()
    {
        $emit("int", 10);
        $emit("integer", (Integer) 10);
        $emit("boolean", false);
        $emit("double", (double) 12);
        $emit("float", (float) 12.5);
        $emit("todo", new Todo("Hello World"));
    }
}
