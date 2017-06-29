package com.axellience.vuegwt.client.gwtextension;

import com.axellience.vuegwt.client.definitions.VueComponentStyle;
import com.axellience.vuegwt.template.TemplateResourceGenerator;
import com.google.gwt.resources.client.ResourcePrototype;
import com.google.gwt.resources.ext.DefaultExtensions;
import com.google.gwt.resources.ext.ResourceGeneratorType;

import java.util.List;

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
    VueComponentStyle getComponentStyle();
}
