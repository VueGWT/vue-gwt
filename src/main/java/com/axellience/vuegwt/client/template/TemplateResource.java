package com.axellience.vuegwt.client.template;

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
@DefaultExtensions(value = { ".txt" })
@ResourceGeneratorType(TemplateResourceGenerator.class)
public interface TemplateResource extends ResourcePrototype
{
    String EXPRESSION_PREFIX       = "VGWT_";
    String EXPRESSION_SUFFIX       = "_EXPR";
    String COLLECTION_ARRAY_SUFFIX = "_ARRAY";

    String getText();
    List<TemplateExpressionBase> getTemplateExpressions();
    Map<String, CssResource> getTemplateStyles();
}
