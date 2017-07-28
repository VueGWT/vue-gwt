package com.axellience.vuegwtexamples.client.examples.common;

/**
 * @author Adrien Baron
 */
public class Todo
{
    private String text;

    public Todo(String s) {
        this.text = s;
    }

    public String getText() {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }
}