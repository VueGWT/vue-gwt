package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.jsnative.VueGwtTools;
import com.axellience.vuegwt.client.jsnative.VueGwtToolsInjector;
import jsinterop.annotations.JsMethod;

/**
 * The Java representation of a VueComponent
 * Whenever you want to add a component to your application you should extends this class.
 *
 * @author Adrien Baron
 */
public abstract class VueComponent
{
    static
    {
        // Inject the JS tools in the page
        VueGwtToolsInjector.inject();
    }

    /**
     * Lifecycle hooks
     * By default they are not copied, they are here to facilitate development
     */
    public void beforeCreate() {

    }
    public void created() {

    }
    public void beforeMount() {

    }
    public void mounted() {

    }
    public void beforeUpdate() {

    }
    public void updated() {

    }
    public void activated() {

    }
    public void deactivated() {

    }
    public void beforeDestroy() {

    }
    public void destroyed() {

    }

    /**
     * Call the given event handler when this event occurs
     * @param eventName Name (identifier) of the event
     * @param onEvent A callback to call when the event occurs
     */
    @JsMethod
    protected final void $on(String eventName, OnEvent onEvent)
    {
        VueGwtTools.vue$on(this, eventName, onEvent);
    }

    /**
     * Emit an event with a given value
     * @param eventName Name (identifier) of the event
     * @param value Value of this event
     */
    @JsMethod
    protected final void $emit(String eventName, Object value)
    {
        VueGwtTools.vue$emit(this, eventName, value);
    }

    /**
     * Emit an event with no value
     * @param eventName Name (identifier) of the event
     */
    protected final void $emit(String eventName)
    {
        this.$emit(eventName, null);
    }
}
