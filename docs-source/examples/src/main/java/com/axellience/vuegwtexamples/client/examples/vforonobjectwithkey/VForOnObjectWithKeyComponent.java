package com.axellience.vuegwtexamples.client.examples.vforonobjectwithkey;

import com.axellience.vuegwt.client.Vue;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwtexamples.client.examples.common.Todo;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
@Component
public class VForOnObjectWithKeyComponent extends Vue
{
    public JsObject<Object> myObject;

    @Override
    public void created() {
        this.myObject = new JsObject<>();
        this.myObject.set("myString", "Hello World");
        this.myObject.set("myInt", 12);
        this.myObject.set("myTodo", new Todo("I'm a Todo"));
    }
}
