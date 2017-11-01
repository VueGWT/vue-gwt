package com.axellience.vuegwt.core.client.template;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.google.gwt.resources.client.CssResource;
import elemental2.core.Function;
import jsinterop.base.JsPropertyMap;

import java.util.Map;

/**
 * Source: GWT Project http://www.gwtproject.org/
 * <p>
 * Modified by Adrien Baron
 */
public interface ComponentTemplate<T extends VueComponent> extends JsPropertyMap<Function>
{
    String EXPRESSION_PREFIX = "exp$";

    default String getRenderFunction()
    {
        return "";
    }
    default String[] getStaticRenderFunctions()
    {
        return null;
    }
    default String[] getTemplateMethods()
    {
        return null;
    }
    default Map<String, CssResource> getTemplateStyles()
    {
        return null;
    }
}
