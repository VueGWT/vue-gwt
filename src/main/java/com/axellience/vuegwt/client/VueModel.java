package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.jsnative.JsObject;
import com.axellience.vuegwt.client.jsnative.VueGwtTools;
import com.axellience.vuegwt.client.jsnative.VueGwtToolsInjector;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

import java.util.Collection;

/**
 * Common base class of VueApp and VueComponent
 * Contains all shared properties between Vue instance (apps) and Components in Vue.JS
 */
public abstract class VueModel
{
    static
    {
        // Inject the JS tools in the page
        VueGwtToolsInjector.inject();
    }

    /**
     * Components registered locally on this VueModel
     */
    @JsProperty
    private final JsObject $$components = new JsObject();

    /**
     * Register VueComponents on this VueModel
     * @param components VueComponents to register on this VueModel
     */
    protected void registerComponents(Collection<VueComponent> components)
    {
        for (VueComponent component : components)
        {
            this.registerComponent(component);
        }
    }

    /**
     * Register a VueComponent on this VueModel
     * @param component VueComponent to register on this VueModel
     */
    protected void registerComponent(VueComponent component)
    {
        this.$$components.set(component.getName(), VueGwtTools.convertFromJavaToVueModel(component));
    }

    /**
     * Emit an event with a given value
     * @param eventName Name (identifier) of the event
     * @param value Value of this event
     */
    @JsMethod
    protected void $emit(String eventName, Object value) {
        // Native method, no code needed
    }

    /**
     * Emit an event with no value
     * @param eventName Name (identifier) of the event
     */
    protected void $emit(String eventName) {
        this.$emit(eventName, null);
    }

    /**
     * Call the given event handler when this event occurs
     * @param eventName Name (identifier) of the event
     * @param onEvent A callback to call when the event occurs
     */
    @JsMethod
    protected void $on(String eventName, OnEvent onEvent) {
        // Native method, no code needed
    }
}
