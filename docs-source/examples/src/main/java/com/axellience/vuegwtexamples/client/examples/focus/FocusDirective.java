package com.axellience.vuegwtexamples.client.examples.focus;

import com.axellience.vuegwt.client.directive.VueDirective;
import com.axellience.vuegwt.client.vnode.VNode;
import com.axellience.vuegwt.client.vnode.VNodeDirective;
import com.axellience.vuegwt.jsr69.directive.annotations.Directive;
import com.google.gwt.dom.client.Element;

/**
 * @author Adrien Baron
 */
@Directive
public class FocusDirective extends VueDirective
{
    @Override
    public void inserted(Element el, VNodeDirective binding, VNode vnode)
    {
        el.focus();
    }
}
