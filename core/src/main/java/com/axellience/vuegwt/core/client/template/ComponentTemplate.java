package com.axellience.vuegwt.core.client.template;

import com.axellience.vuegwt.core.client.component.VueComponent;
import elemental2.core.Function;
import jsinterop.base.JsPropertyMap;

/**
 * An interface representing an Vue Component template once processed and compiled.
 */
public interface ComponentTemplate<T extends VueComponent> extends JsPropertyMap<Function>
{
    String EXPRESSION_PREFIX = "exp$";
    String TEMPLATE_EXTENSION = ".html";

    String getRenderFunction();
    String[] getStaticRenderFunctions();
    int getTemplateMethodsCount();
}
