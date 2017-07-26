package com.axellience.vuegwt.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * @author Adrien Baron
 */
public interface VueLibResources extends ClientBundle
{
    VueLibResources LIB_RESOURCES = GWT.create(VueLibResources.class);

    @Source("vue.min.js")
    TextResource vueScript();
}