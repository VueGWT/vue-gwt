package com.axellience.vuegwtexamples.client.examples.focus;

import com.axellience.vuegwt.client.VueDirective;
import com.axellience.vuegwt.client.VueDirectiveBinding;
import com.axellience.vuegwt.jsr69.directive.annotations.Directive;
import com.google.gwt.dom.client.Element;

/**
 * @author Adrien Baron
 */
@Directive
public class FocusDirective extends VueDirective
{
    @Override
    public void inserted(Element el, VueDirectiveBinding binding)
    {
        el.focus();
    }
}
