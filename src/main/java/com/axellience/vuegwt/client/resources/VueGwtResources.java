package com.axellience.vuegwt.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * Resources used by VueComponent GWT
 * @author Adrien Baron
 */
public interface VueGwtResources extends ClientBundle
{
    @Source("vue-gwt.min.js")
    TextResource vueGWTScript();

    @Source("js-tools.min.js")
    TextResource jsToolsScript();
}
