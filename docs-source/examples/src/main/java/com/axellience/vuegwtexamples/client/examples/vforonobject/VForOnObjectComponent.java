package com.axellience.vuegwtexamples.client.examples.vforonobject;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.hooks.HasCreated;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwtexamples.client.examples.common.Todo;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class VForOnObjectComponent extends VueComponent implements HasCreated
{
    @JsProperty JsObject<Object> myObject;

    @Override
    public void created()
    {
        this.myObject = new JsObject<>();
        this.myObject.set("myString", "Hello World");
        this.myObject.set("myInt", 12);
        this.myObject.set("myTodo", new Todo("I'm a Todo"));

        JsObject<JsObject<Object>> myJsObj = new JsObject<>();
        myJsObj.set("bob", myObject);
    }
}
