package com.axellience.vuegwt.client.component.template;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.template.ComponentWithTemplateResourceGenerator;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ResourcePrototype;
import com.google.gwt.resources.ext.DefaultExtensions;
import com.google.gwt.resources.ext.ResourceGeneratorType;

import java.util.Map;

/**
 * Source: GWT Project http://www.gwtproject.org/
 * <p>
 * Modified by Adrien Baron
 */
@DefaultExtensions(value = { ".html" })
@ResourceGeneratorType(ComponentWithTemplateResourceGenerator.class)
public interface ComponentWithTemplate<T extends VueComponent> extends ResourcePrototype
{
    String EXPRESSION_PREFIX = "exp$";

    default String getRenderFunction()
    {
        return null;
    }
    default String[] getStaticRenderFunctions()
    {
        return null;
    }
    default String[] getTemplateComputedProperties()
    {
        return null;
    }
    default Map<String, CssResource> getTemplateStyles()
    {
        return null;
    }

    @Override
    default String getName()
    {
        return null;
    }
}
