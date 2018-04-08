package com.axellience.vuegwt.tests.client.components.events.types;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import com.axellience.vuegwt.tests.client.common.Todo;

@Component
public class EmitTypesChildComponent implements IsVueComponent, HasCreated
{
    @Override
    public void created()
    {
        asVue().$emit("int", 10);
        asVue().$emit("integer", (Integer) 10);
        asVue().$emit("boolean", false);
        asVue().$emit("double", (double) 12);
        asVue().$emit("float", (float) 12.5);
        asVue().$emit("todo", new Todo("Hello World"));
    }
}