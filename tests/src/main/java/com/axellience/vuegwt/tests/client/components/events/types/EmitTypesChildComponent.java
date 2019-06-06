package com.axellience.vuegwt.tests.client.components.events.types;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import com.axellience.vuegwt.tests.client.common.Todo;
import jsinterop.base.Js;

@Component
public class EmitTypesChildComponent implements IsVueComponent, HasCreated {

  @Override
  public void created() {
    vue().$emit("int", 10);
    vue().$emit("integer", (Integer) 10);
    vue().$emit("boolean", false);
    vue().$emit("double", (double) 12);
    vue().$emit("float", (float) 12.5);
    vue().$emit("todo", new Todo("Hello World"));

    vue().$emit("intAsAny", Js.asAny(10));
    vue().$emit("integerAsAny", Js.asAny((Integer) 10));
    vue().$emit("booleanAsAny", Js.asAny(false));
    vue().$emit("doubleAsAny", Js.asAny(12));
    vue().$emit("floatAsAny", Js.asAny(12.5));
    vue().$emit("todoAsAny", Js.asAny(new Todo("Hello World")));
  }
}