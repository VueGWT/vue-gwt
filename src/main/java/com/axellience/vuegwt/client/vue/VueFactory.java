package com.axellience.vuegwt.client.vue;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.JsComponent;

/**
 * A factory to create Vue Components.
 * All the {@link Component} and {@link JsComponent} get a generated VueJsConstructor.
 * It can be injected with Gin.
 * @author Adrien Baron
 */
public class VueFactory<T extends VueComponent>
{
    protected VueJsConstructor<T> jsConstructor;

    public final T create()
    {
        return jsConstructor.instantiate();
    }

    public VueJsConstructor<T> getJsConstructor()
    {
        return jsConstructor;
    }
}
