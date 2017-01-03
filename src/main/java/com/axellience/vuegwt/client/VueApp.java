package com.axellience.vuegwt.client;

import com.google.gwt.dom.client.Element;
import jsinterop.annotations.JsProperty;

/**
 * The root of a Vue application
 * When you want to create your app, you should extends this class.
 * Then pass an instance of your app to to Vue.app() to boostrap your app.
 */
public abstract class VueApp extends VueModel
{
    @JsProperty
    private Object       $$el;

    /**
     * Init the application on the given element
     * @param element A GWT DOM Element
     */
    protected void init(Element element)
    {
        this.$$el = element;
    }

    /**
     * Init the application on the element indicated by the given selector
     * @param element Element CSS selector
     */
    protected void init(String element)
    {
        this.$$el = element;
    }
}
