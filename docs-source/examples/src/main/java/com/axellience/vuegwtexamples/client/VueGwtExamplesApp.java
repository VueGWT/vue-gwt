package com.axellience.vuegwtexamples.client;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.jsnative.JsTools;
import com.axellience.vuegwt.client.jsnative.Vue;
import com.axellience.vuegwtexamples.client.examples.example1.Example1Component;
import com.axellience.vuegwtexamples.client.examples.example2.Example2Component;
import com.axellience.vuegwtexamples.client.examples.example3.Example3Component;
import com.axellience.vuegwtexamples.client.examples.example4.Example4Component;
import com.axellience.vuegwtexamples.client.examples.example5.Example5Component;
import com.axellience.vuegwtexamples.client.examples.example6.Example6Component;
import com.axellience.vuegwtexamples.client.examples.example7.Example7Component;
import com.axellience.vuegwtexamples.client.examples.example8.Example8Component;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;

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
        this.addExample("example1", Example1Component.class);
        this.addExample("example2", Example2Component.class);
        this.addExample("example3", Example3Component.class);
        this.addExample("example4", Example4Component.class);
        this.addExample("example5", Example5Component.class);
        this.addExample("example6", Example6Component.class);
        this.addExample("example7", Example7Component.class);
        this.addExample("example8", Example8Component.class);
    }

    private void addExample(String exampleId, Class<? extends VueComponent> exampleClass)
    {
        // If we find the containing div for this example, we instantiate it
        if (Document.get().getElementById(exampleId) != null)
        {
            VueComponent exampleInstance = Vue.attach("#" + exampleId, exampleClass);
            JsTools.setObjectProperty(JsTools.getWindow(), exampleId, exampleInstance);
        }
    }
}
