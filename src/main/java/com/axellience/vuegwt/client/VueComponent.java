package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.jsnative.JsObject;
import com.axellience.vuegwt.client.jsnative.Vue;
import com.axellience.vuegwt.client.jsnative.VueGwtTools;
import com.axellience.vuegwt.client.jsnative.VueGwtToolsInjector;
import com.axellience.vuegwt.client.gwtextension.TemplateResource;
import com.google.gwt.dom.client.Element;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

/**
 * The Java representation of a VueComponent
 * Whenever you want to add a component to your application you should extends this class.
 * Components should be registered either by calling Vue.component or registerComponent on another
 * component (local registration)
 */
public abstract class VueComponent
{
    static
    {
        // Inject the JS tools in the page
        VueGwtToolsInjector.inject();
    }

    /**
     * The component element
     * Only useful if you add your component using at
     */
    @JsProperty
    private Object $$vue_el;

    /**
     * The template of this Vue component this should be an html file next to your component class
     * You then register it as a TextResource and pass it in your component setTemplate function
     */
    @JsProperty
    private String $$vue_template;

    /**
     * Props of your component (data it can receive as attributes from other components)
     */
    @JsProperty
    private final JsObject $$vue_props = new JsObject();

    /**
     * Components registered locally
     */
    @JsProperty
    private final JsObject $$vue_components = new JsObject();

    /**
     * Directives registered locally
     */
    @JsProperty
    private final JsObject $$vue_directives = new JsObject();

    /**
     * Register a VueComponent locally
     * @param componentClass VueComponent to register
     */
    public final void registerComponent(Class<? extends VueComponent> componentClass)
    {
        this.$$vue_components.set(
            VueGwtTools.componentToTagName(componentClass),
            Vue.getJsComponentDefinitionForClass(componentClass)
        );
    }

    /**
     * Register a VueDirective locally
     * @param directive VueDirective to register
     */
    protected final void registerDirective(VueDirective directive)
    {
        this.$$vue_directives.set(
            VueGwtTools.directiveToTagName(directive),
            VueGwtTools.javaDirectiveToVueDirectiveDefinition(directive)
        );
    }

    /**
     * Set the element this Component is attached to
     * @param el A CSS selector for this element
     */
    public void setEl(String el)
    {
        this.$$vue_el = el;
    }

    /**
     * Set the element this Component is attached to
     * @param el The DOM Element
     */
    public void setEl(Element el)
    {
        this.$$vue_el = el;
    }

    /**
     * Set the template to use for this Component
     * @param template
     */
    public void setTemplate(TemplateResource template)
    {
        this.setTemplate(template.getText());
    }

    /**
     * Set the template to use for this Component
     * @param template
     */
    protected void setTemplate(String template)
    {
        this.$$vue_template = template;
    }

    /**
     * Used to add a property to a Component
     * Doesn't support constraint yet.
     * @param prop Name of the custom property
     */
    public final void addProp(String prop)
    {
        this.$$vue_props.set(prop, null);
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
