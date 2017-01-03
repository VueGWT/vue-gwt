package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.jsnative.JsObject;
import com.google.gwt.resources.client.TextResource;
import jsinterop.annotations.JsProperty;

/**
 * The Java representation of a VueComponent
 * Whenever you want to add a component to your application you should extends this class.
 * Components should be registered either by calling Vue.component or registerComponent on another
 * component (local registration)
 */
public abstract class VueComponent extends VueModel
{
    /**
     * The template of this Vue component this should be an html file next to your component class
     * You then register it as a TextResource and pass it in your component init function
     */
    @JsProperty
    private String   $$template;

    /**
     * The component name (html tag), it's recommended to use kebab case (my-component)
     */
    @JsProperty
    private String   $$name;

    /**
     * Props of your component (data it can receive as attributes from other components)
     */
    @JsProperty
    private final JsObject $$props = new JsObject();

    /**
     * Init method, should be called in your component constructor
     * @param name The name (tag) of your component
     * @param template Contains your component template
     */
    protected void init(String name, TextResource template)
    {
        this.$$name = name;
        this.$$template = template.getText();
    }

    public String getName()
    {
        return $$name;
    }

    /**
     * Used to add a property to a component
     * Doesn't support constraint yet.
     * @param prop Name of the custom property
     */
    protected final void addProp(String prop)
    {
        this.$$props.set(prop, null);
    }
}
