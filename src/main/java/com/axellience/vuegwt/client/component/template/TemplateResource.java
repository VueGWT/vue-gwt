package com.axellience.vuegwt.client.component.template;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.template.TemplateResourceGwtGenerator;
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
@ResourceGeneratorType(TemplateResourceGwtGenerator.class)
public interface TemplateResource<T extends VueComponent> extends ResourcePrototype
{
    String EXPRESSION_PREFIX = "exp$";

    String getRenderFunction();
    String[] getStaticRenderFunctions();
    String[] getTemplateComputedProperties();
    String[] getTemplateMethods();
    Map<String, CssResource> getTemplateStyles();
}
