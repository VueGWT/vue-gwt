package com.axellience.vuegwtexamples.client;

import com.axellience.vuegwt.client.Vue;
import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.tools.JsTools;
import com.axellience.vuegwt.client.vue.VueConstructor;
import com.axellience.vuegwtexamples.client.examples.bindinlinestyle.BindInlineStyleComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.buttonplusone.ButtonPlusOneComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.canhide.CanHideComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.counterwithevent.CounterWithEventComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.evennumbers.EvenNumbersComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.exclamation.ExclamationComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.extendjavacomponent.ChildComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.extendsjscomponent.ChildJavaComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.focus.FocusDirectiveComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.greet.GreetComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.instanciatejscomponent.FullJsWithMethodsComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.kitten.KittenComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.link.LinkComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.melisandre.MelisandreComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.message.MessageComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.parent.ParentComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.recursive.RecursiveComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.reverse.ReverseComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.shareddatamodel.SharedDataModelComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.simplelink.SimpleLinkComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.simplerender.RenderAppComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.simpletodolist.SimpleTodoListComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.todolist.TodoListComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.todotext.TodoTextComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.todotextcomputed.TodoTextComputedComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.tree.TreeComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.vforonobject.VForOnObjectComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.vforonobjectwithkey.VForOnObjectWithKeyComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.vforonobjectwithkeyandindex.VForOnObjectWithKeyAndIndexComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.vforwithindex.VForWithIndexComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.vforwithrange.VForWithRangeComponentConstructor;
import com.axellience.vuegwtexamples.client.examples.vonwithdomevent.VOnWithDOMEventComponentConstructor;
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
        VueConstructor vueClass = (VueConstructor) JsTools.getWindow().get("FullJsComponent");
        VueComponent myComponent = vueClass.instantiate();
        myComponent.$mount("#fullJsComponent");

        this.addExample("simpleLinkComponent", SimpleLinkComponentConstructor.get());
        this.addExample("linkComponent", LinkComponentConstructor.get());
        this.addExample("canHideComponent", CanHideComponentConstructor.get());
        this.addExample("simpleTodoListComponent", SimpleTodoListComponentConstructor.get());
        this.addExample("exclamationComponent", ExclamationComponentConstructor.get());
        this.addExample("messageComponent", MessageComponentConstructor.get());
        this.addExample("parentComponent", ParentComponentConstructor.get());
        this.addExample("todoListComponent", TodoListComponentConstructor.get());
        this.addExample("melisandreComponent", MelisandreComponentConstructor.get());
        this.addExample("kittenComponent", KittenComponentConstructor.get());
        this.addExample("reverseComponent", ReverseComponentConstructor.get());
        this.addExample("vForWithIndexComponent", VForWithIndexComponentConstructor.get());
        this.addExample("vForWithRangeComponent", VForWithRangeComponentConstructor.get());
        this.addExample("vForOnObjectComponent", VForOnObjectComponentConstructor.get());
        this.addExample("vForOnObjectWithKeyComponent",
            VForOnObjectWithKeyComponentConstructor.get());
        this.addExample("vForOnObjectWithKeyAndIndexComponent",
            VForOnObjectWithKeyAndIndexComponentConstructor.get());
        this.addExample("evenNumbersComponent", EvenNumbersComponentConstructor.get());
        this.addExample("bindInlineStyleComponent", BindInlineStyleComponentConstructor.get());
        this.addExample("buttonPlusOneComponent", ButtonPlusOneComponentConstructor.get());
        this.addExample("greetComponent", GreetComponentConstructor.get());
        this.addExample("vOnWithDOMEventComponent", VOnWithDOMEventComponentConstructor.get());
        this.addExample("todoTextComponent", TodoTextComponentConstructor.get());
        this.addExample("todoTextComputedComponent", TodoTextComputedComponentConstructor.get());
        this.addExample("sharedDataModelComponent1", SharedDataModelComponentConstructor.get());
        this.addExample("sharedDataModelComponent2", SharedDataModelComponentConstructor.get());
        this.addExample("sharedDataModelComponent3", SharedDataModelComponentConstructor.get());
        this.addExample("counterWithEventComponent", CounterWithEventComponentConstructor.get());
        this.addExample("treeComponent", TreeComponentConstructor.get());
        this.addExample("recursiveComponent", RecursiveComponentConstructor.get());
        this.addExample("focusDirectiveComponent", FocusDirectiveComponentConstructor.get());
        this.addExample("renderAppComponent", RenderAppComponentConstructor.get());
        this.addExample("extendJavaComponent", ChildComponentConstructor.get());
        this.addExample("extendJsComponent", ChildJavaComponentConstructor.get());
        this.addExample("fullJsWithMethodsComponent", FullJsWithMethodsComponentConstructor.get());
    }

    private void addExample(String exampleId, VueConstructor<? extends VueComponent> exampleVuConstructor)
    {
        // If we find the containing div for this example, we instantiate it
        if (Document.get().getElementById(exampleId) != null)
        {
            VueComponent exampleInstance = Vue.attach("#" + exampleId, exampleVuConstructor);
            JsTools.getWindow().set(exampleId, exampleInstance);
        }
    }
}
