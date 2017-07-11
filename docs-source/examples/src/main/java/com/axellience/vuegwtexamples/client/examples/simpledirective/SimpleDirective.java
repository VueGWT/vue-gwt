package com.axellience.vuegwtexamples.client.examples.simpledirective;

import com.axellience.vuegwt.client.VueDirective;
import com.axellience.vuegwt.client.VueDirectiveBinding;
import com.axellience.vuegwt.client.jsnative.JsTools;
import com.axellience.vuegwt.jsr69.directive.annotations.Directive;
import com.google.gwt.dom.client.Element;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
@Directive
public class SimpleDirective extends VueDirective
{
    @Override
    public void bind(Element el, VueDirectiveBinding binding)
    {
        JsTools.log(el);
    }
}
