package com.axellience.vuegwt.tests.client;

import com.axellience.vuegwt.core.client.VueGWT;
import com.google.gwt.core.client.EntryPoint;
import elemental2.core.Function;
import elemental2.dom.DomGlobal;
import jsinterop.base.JsPropertyMap;

/**
 * Entry point class
 */
public class VueGwtTestsApp implements EntryPoint
{
    /**
     * This is the entry point method.
     */
    public void onModuleLoad()
    {
        VueGWT.init();

        // Call on ready
        ((JsPropertyMap<Function>) DomGlobal.window).get("onVueGwtTestsReady").call();
    }
}
