package com.axellience.vuegwtexamples.client.examples.vforonobjectwithkeyandindex;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwtexamples.client.examples.common.Todo;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class VForOnObjectWithKeyAndIndexComponent extends VueComponent
{
    @JsProperty JsObject<Object> myObject;

    public VForOnObjectWithKeyAndIndexComponent() {
        this.myObject = new JsObject<>();
        this.myObject.set("myString", "Hello World");
        this.myObject.set("myInt", 12);
        this.myObject.set("myTodo", new Todo("I'm a Todo"));
    }
}
