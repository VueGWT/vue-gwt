package com.axellience.vuegwtexamples.client.examples.focus;

import com.axellience.vuegwt.core.annotations.directive.Directive;
import com.axellience.vuegwt.core.client.directive.VueDirective;
import com.axellience.vuegwt.core.client.vnode.VNode;
import com.axellience.vuegwt.core.client.vnode.VNodeDirective;
import elemental2.dom.Element;

/**
 * @author Adrien Baron
 */
@Directive
public class FocusDirective extends VueDirective {

  @Override
  public void inserted(Element el, VNodeDirective binding, VNode vnode) {
    el.focus();
  }
}
