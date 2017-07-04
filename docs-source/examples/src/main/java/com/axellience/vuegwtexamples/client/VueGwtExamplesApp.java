package com.axellience.vuegwtexamples.client;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.jsnative.JsTools;
import com.axellience.vuegwt.client.jsnative.Vue;
import com.axellience.vuegwtexamples.client.examples.example1.Example1Component;
import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class VueGwtExamplesApp implements EntryPoint {
    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        this.addExample("example1", Example1Component.class);
    }

    private void addExample(String exampleId, Class<? extends VueComponent> exampleClass) {
        VueComponent exampleInstance = Vue.attach("#" + exampleId, exampleClass);
        JsTools.setObjectProperty(JsTools.getWindow(), exampleId, exampleInstance);
    }
}
