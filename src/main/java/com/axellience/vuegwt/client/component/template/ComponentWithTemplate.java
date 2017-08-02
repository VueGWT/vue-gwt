package com.axellience.vuegwt.client.component.template;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.template.TemplateResourceGenerator;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ResourcePrototype;
import com.google.gwt.resources.ext.DefaultExtensions;
import com.google.gwt.resources.ext.ResourceGeneratorType;

import java.util.List;
import java.util.Map;

/**
 * Source: GWT Project http://www.gwtproject.org/
 * <p>
 * Modified by Adrien Baron
 */
@DefaultExtensions(value = { ".html" })
@ResourceGeneratorType(TemplateResourceGenerator.class)
public interface ComponentWithTemplate<T extends VueComponent> extends ResourcePrototype
{
    String EXPRESSION_PREFIX = "exp$";

    String getRenderFunction();
    String[] getStaticRenderFunctions();
    List<TemplateExpressionBase> getTemplateExpressions();
    Map<String, CssResource> getTemplateStyles();
}
