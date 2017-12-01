package com.axellience.vuegwt.core.client.template;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.google.gwt.resources.client.CssResource;
import elemental2.core.Function;
import jsinterop.base.JsPropertyMap;

import java.util.Map;

/**
 * An interface representing an Vue Component template once processed and compiled.
 */
public interface ComponentTemplate<T extends VueComponent> extends JsPropertyMap<Function>
{
    String EXPRESSION_PREFIX = "exp$";
    String TEMPLATE_EXTENSION = ".html";

    String getRenderFunction();
    String[] getStaticRenderFunctions();
    Map<String, CssResource> getTemplateStyles();
    int getTemplateMethodsCount();
}
