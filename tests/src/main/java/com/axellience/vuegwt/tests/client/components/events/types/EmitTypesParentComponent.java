package com.axellience.vuegwt.tests.client.components.events.types;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.tests.client.common.Todo;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

@Component(components = { EmitTypesChildComponent.class })
class EmitTypesParentComponent implements IsVueComponent
{
    @JsProperty public int myInt; // 10
    @JsProperty public boolean myBoolean; // false
    @JsProperty public double myDouble; // 12
    @JsProperty public float myFloat; // 12.5
    @JsProperty public Todo myTodo; // "Hello World"

    private Integer myInteger; // 10

    @JsMethod
    public void setInt(int eventInt)
    {
        this.myInt = eventInt;
    }

    @JsMethod
    public void setInteger(Integer eventInteger)
    {
        this.myInteger = eventInteger;
    }

    @JsMethod
    public void setBoolean(boolean eventBoolean)
    {
        this.myBoolean = eventBoolean;
    }

    @JsMethod
    public void setDouble(double eventDouble)
    {
        this.myDouble = eventDouble;
    }

    @JsMethod
    public void setFloat(float eventFloat)
    {
        this.myFloat = eventFloat;
    }

    @JsMethod
    public void setTodo(Todo eventTodo)
    {
        this.myTodo = eventTodo;
    }

    @JsMethod
    public Integer getMyInteger()
    {
        return myInteger;
    }

    @JsMethod
    public Integer getTestIntegerValue()
    {
        return 10;
    }
}
