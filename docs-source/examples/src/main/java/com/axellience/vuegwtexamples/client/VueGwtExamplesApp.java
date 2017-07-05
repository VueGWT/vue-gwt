package com.axellience.vuegwtexamples.client;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.jsnative.JsTools;
import com.axellience.vuegwt.client.jsnative.Vue;
import com.axellience.vuegwtexamples.client.examples.example1.SimpleLinkComponent;
import com.axellience.vuegwtexamples.client.examples.example10.KittenComponent;
import com.axellience.vuegwtexamples.client.examples.example2.LinkComponent;
import com.axellience.vuegwtexamples.client.examples.example3.CanHideComponent;
import com.axellience.vuegwtexamples.client.examples.example4.SimpleTodoListComponent;
import com.axellience.vuegwtexamples.client.examples.example5.ExclamationComponent;
import com.axellience.vuegwtexamples.client.examples.example6.MessageComponent;
import com.axellience.vuegwtexamples.client.examples.example7.ParentComponent;
import com.axellience.vuegwtexamples.client.examples.example8.TodoListComponent;
import com.axellience.vuegwtexamples.client.examples.example9.MelisandreComponent;
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
        this.addExample("simpleLinkComponent", SimpleLinkComponent.class);
        this.addExample("linkComponent", LinkComponent.class);
        this.addExample("canHideComponent", CanHideComponent.class);
        this.addExample("simpleTodoListComponent", SimpleTodoListComponent.class);
        this.addExample("exclamationComponent", ExclamationComponent.class);
        this.addExample("messageComponent", MessageComponent.class);
        this.addExample("parentComponent", ParentComponent.class);
        this.addExample("todoListComponent", TodoListComponent.class);
        this.addExample("melisandreComponent", MelisandreComponent.class);
        this.addExample("kittenComponent", KittenComponent.class);
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
