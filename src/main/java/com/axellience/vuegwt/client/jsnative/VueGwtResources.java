package com.axellience.vuegwt.client.jsnative;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * Resources used by Vue GWT
 */
public interface VueGwtResources extends ClientBundle
{
    VueGwtResources JS_RESOURCES = GWT.create(VueGwtResources.class);

    @Source("JsTools.js")
    TextResource jsToolsScript();

    @Source("VueGwtTools.js")
    TextResource vueToolsScript();
}
