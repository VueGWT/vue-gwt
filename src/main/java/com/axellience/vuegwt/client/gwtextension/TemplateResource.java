package com.axellience.vuegwt.client.gwtextension;

import com.google.gwt.resources.client.ResourcePrototype;
import com.google.gwt.resources.ext.DefaultExtensions;
import com.google.gwt.resources.ext.ResourceGeneratorType;

/**
 * Source: GWT Project http://www.gwtproject.org/
 * <p>
 * Modified by Adrien Baron
 */
@DefaultExtensions(value = { ".txt" })
@ResourceGeneratorType(TemplateResourceGenerator.class)
public interface TemplateResource extends ResourcePrototype
{
    String EXPRESSION_PREFIX = "$$VUE_GWT_EXPR_";

    String getText();
}
