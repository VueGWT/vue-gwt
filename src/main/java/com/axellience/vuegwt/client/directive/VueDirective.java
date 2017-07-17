package com.axellience.vuegwt.client.directive;

import com.axellience.vuegwt.client.vnode.VNode;
import com.axellience.vuegwt.client.vnode.VNodeDirective;
import com.google.gwt.dom.client.Element;
import jsinterop.annotations.JsType;

/**
 * A Vue Directive. Allows you to add behavior to DOM elements
 */
@JsType
public abstract class VueDirective
{
    public void bind(Element el, VNodeDirective binding, VNode vnode)
    {
        // Do nothing, override to add behavior
    }

    public void inserted(Element el, VNodeDirective binding, VNode vnode)
    {
        // Do nothing, override to add behavior
    }

    public void update(Element el, VNodeDirective binding, VNode vnode, VNode oldVnode)
    {
        // Do nothing, override to add behavior
    }

    public void componentUpdated(Element el, VNodeDirective binding, VNode vnode, VNode oldVnode)
    {
        // Do nothing, override to add behavior
    }

    public void unbind(Element el, VNodeDirective binding, VNode vnode)
    {
        // Do nothing, override to add behavior
    }
}
