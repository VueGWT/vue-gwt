package com.axellience.vuegwt.client;

import com.google.gwt.dom.client.Element;
import jsinterop.annotations.JsType;

/**
 * A Vue Directive. Allows you to add behavior to DOM elements
 */
@JsType
public abstract class VueDirective
{
    public void bind(Element el, VueDirectiveBinding binding) {
        // Do nothing, override to add behavior
    }
    public void inserted(Element el, VueDirectiveBinding binding) {
        // Do nothing, override to add behavior
    }
    public void update(Element el, VueDirectiveBinding binding) {
        // Do nothing, override to add behavior
    }
    public void componentUpdated(Element el, VueDirectiveBinding binding) {
        // Do nothing, override to add behavior
    }
    public void unbind(Element el, VueDirectiveBinding binding) {
        // Do nothing, override to add behavior
    }
}
