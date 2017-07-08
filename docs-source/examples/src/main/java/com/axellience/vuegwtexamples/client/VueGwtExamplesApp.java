package com.axellience.vuegwtexamples.client;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.jsnative.JsTools;
import com.axellience.vuegwt.client.jsnative.Vue;
import com.axellience.vuegwtexamples.client.examples.bindinlinestyle.BindInlineStyleComponent;
import com.axellience.vuegwtexamples.client.examples.buttonplusone.ButtonPlusOneComponent;
import com.axellience.vuegwtexamples.client.examples.canhide.CanHideComponent;
import com.axellience.vuegwtexamples.client.examples.evennumbers.EvenNumbersComponent;
import com.axellience.vuegwtexamples.client.examples.exclamation.ExclamationComponent;
import com.axellience.vuegwtexamples.client.examples.greet.GreetComponent;
import com.axellience.vuegwtexamples.client.examples.kitten.KittenComponent;
import com.axellience.vuegwtexamples.client.examples.link.LinkComponent;
import com.axellience.vuegwtexamples.client.examples.melisandre.MelisandreComponent;
import com.axellience.vuegwtexamples.client.examples.message.MessageComponent;
import com.axellience.vuegwtexamples.client.examples.parent.ParentComponent;
import com.axellience.vuegwtexamples.client.examples.reverse.ReverseComponent;
import com.axellience.vuegwtexamples.client.examples.simplelink.SimpleLinkComponent;
import com.axellience.vuegwtexamples.client.examples.simpletodolist.SimpleTodoListComponent;
import com.axellience.vuegwtexamples.client.examples.todolist.TodoListComponent;
import com.axellience.vuegwtexamples.client.examples.vforonobject.VForOnObjectComponent;
import com.axellience.vuegwtexamples.client.examples.vforonobjectwithkey.VForOnObjectWithKeyComponent;
import com.axellience.vuegwtexamples.client.examples.vforonobjectwithkeyandindex.VForOnObjectWithKeyAndIndexComponent;
import com.axellience.vuegwtexamples.client.examples.vforwithindex.VForWithIndexComponent;
import com.axellience.vuegwtexamples.client.examples.vforwithrange.VForWithRangeComponent;
import com.axellience.vuegwtexamples.client.examples.vonwithdomevent.VOnWithDOMEventComponent;
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
        this.addExample("reverseComponent", ReverseComponent.class);
        this.addExample("vForWithIndexComponent", VForWithIndexComponent.class);
        this.addExample("vForWithRangeComponent", VForWithRangeComponent.class);
        this.addExample("vForOnObjectComponent", VForOnObjectComponent.class);
        this.addExample("vForOnObjectWithKeyComponent", VForOnObjectWithKeyComponent.class);
        this.addExample("vForOnObjectWithKeyAndIndexComponent",
            VForOnObjectWithKeyAndIndexComponent.class);
        this.addExample("evenNumbersComponent", EvenNumbersComponent.class);
        this.addExample("bindInlineStyleComponent", BindInlineStyleComponent.class);
        this.addExample("buttonPlusOneComponent", ButtonPlusOneComponent.class);
        this.addExample("greetComponent", GreetComponent.class);
        this.addExample("vOnWithDOMEventComponent", VOnWithDOMEventComponent.class);
    }

    private void addExample(String exampleId, Class<? extends VueComponent> exampleClass)
    {
        // If we find the containing div for this example, we instantiate it
        if (Document.get().getElementById(exampleId) != null)
        {
            VueComponent exampleInstance = Vue.attach("#" + exampleId, exampleClass);
            JsTools.getWindow().set(exampleId, exampleInstance);
        }
    }
}
