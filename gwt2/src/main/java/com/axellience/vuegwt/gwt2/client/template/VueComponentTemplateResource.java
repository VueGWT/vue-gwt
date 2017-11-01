package com.axellience.vuegwt.gwt2.client.template;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.client.template.ComponentTemplate;
import com.axellience.vuegwt.gwt2.template.TemplateResourceGwtGenerator;
import com.google.gwt.resources.client.ResourcePrototype;
import com.google.gwt.resources.ext.DefaultExtensions;
import com.google.gwt.resources.ext.ResourceGeneratorType;
import elemental2.core.Function;
import jsinterop.base.JsPropertyMap;

/**
 * Source: GWT Project http://www.gwtproject.org/
 * <p>
 * Modified by Adrien Baron
 */
@DefaultExtensions(value = { ".html" })
@ResourceGeneratorType(TemplateResourceGwtGenerator.class)
public interface VueComponentTemplateResource<T extends VueComponent>
    extends ComponentTemplate<T>, ResourcePrototype, JsPropertyMap<Function>
{
    @Override
    default String getName()
    {
        return "";
    }
}
