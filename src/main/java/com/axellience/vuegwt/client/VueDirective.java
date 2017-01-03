package com.axellience.vuegwt.client;

import jsinterop.annotations.JsProperty;

/**
 * A Vue Directive. Allows you to add behavior to DOM elements
 */
public abstract class VueDirective
{
    /**
     * The component name (html tag), it's recommended to use kebab case (my-component)
     */
    @JsProperty
    private String   $$name;

    protected void init(String name) {
        this.$$name = name;
    }

    public String getName()
    {
        return $$name;
    }
}
