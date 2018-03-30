package com.axellience.vuegwt.tests.client.common;

import jsinterop.annotations.JsProperty;

public class Todo
{
    @JsProperty
    private String text;

    public Todo(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }
}
