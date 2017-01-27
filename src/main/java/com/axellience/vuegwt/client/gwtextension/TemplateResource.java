package com.axellience.vuegwt.client.gwtextension;

import com.google.gwt.resources.client.ResourcePrototype;
import com.google.gwt.resources.ext.DefaultExtensions;
import com.google.gwt.resources.ext.ResourceGeneratorType;

/**
 * A resource that contains text that should be incorporated into the compiled
 * output.
 */
@DefaultExtensions(value = {".txt"})
@ResourceGeneratorType(TemplateResourceGenerator.class)
public interface TemplateResource extends ResourcePrototype
{
    String getText();
}
