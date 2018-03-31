package com.axellience.vuegwtexamples.client.examples.vforonobjectwithkeyandindex;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwtexamples.client.examples.common.Todo;
import jsinterop.annotations.JsProperty;
import jsinterop.base.JsPropertyMap;

/**
 * @author Adrien Baron
 */
@Component
public class VForOnObjectWithKeyAndIndexComponent implements IsVueComponent , HasCreated
{
    @JsProperty JsPropertyMap<Object> myObject = JsPropertyMap.of();

    @Override
    public void created()
    {
        this.myObject.set("myString", "Hello World");
        this.myObject.set("myInt", 12);
        this.myObject.set("myTodo", new Todo("I'm a Todo"));
    }
}
