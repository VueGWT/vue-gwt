package com.axellience.vuegwtexamples.client.examples.vforonobject;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.hooks.HasCreated;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwtexamples.client.examples.common.Todo;
import jsinterop.annotations.JsProperty;
import jsinterop.base.JsPropertyMap;

/**
 * @author Adrien Baron
 */
@Component
public class VForOnObjectComponent extends VueComponent implements HasCreated
{
    @JsProperty JsPropertyMap<Object> myObject;

    @Override
    public void created()
    {
        this.myObject = JsPropertyMap.of();
        this.myObject.set("myString", "Hello World");
        this.myObject.set("myInt", 12);
        this.myObject.set("myTodo", new Todo("I'm a Todo"));
    }
}
