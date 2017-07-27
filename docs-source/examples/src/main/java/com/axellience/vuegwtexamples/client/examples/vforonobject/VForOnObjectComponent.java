package com.axellience.vuegwtexamples.client.examples.vforonobject;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwtexamples.client.examples.common.Todo;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
@Component
public class VForOnObjectComponent extends VueComponent
{
    public JsObject<Object> myObject;

    public VForOnObjectComponent() {
        this.myObject = new JsObject<>();
        this.myObject.set("myString", "Hello World");
        this.myObject.set("myInt", 12);
        this.myObject.set("myTodo", new Todo("I'm a Todo"));

        JsObject<JsObject<Object>> myJsObj = new JsObject<>();
        myJsObj.set("bob", myObject);
    }
}
