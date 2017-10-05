package com.axellience.vuegwtexamples.client;

import com.axellience.vuegwt.client.VueGWT;
import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class VueGwtExamplesApp implements EntryPoint
{
    /**
     * This is the entry point method.
     */
    public void onModuleLoad()
    {
        VueGWT.initWithoutVueLib();
        VueGwtExamplesService.initExamples();
    }
}
