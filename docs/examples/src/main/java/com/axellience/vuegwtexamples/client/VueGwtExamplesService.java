package com.axellience.vuegwtexamples.client;

import com.axellience.vuegwt.core.client.Vue;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.vue.VueComponentFactory;
import com.axellience.vuegwt.core.client.vue.VueJsConstructor;
import com.axellience.vuegwtexamples.client.examples.bindinlinestyle.BindInlineStyleComponentFactory;
import com.axellience.vuegwtexamples.client.examples.buttonplusone.ButtonPlusOneComponentFactory;
import com.axellience.vuegwtexamples.client.examples.canhide.CanHideComponentFactory;
import com.axellience.vuegwtexamples.client.examples.counterwithevent.CounterWithEventComponentFactory;
import com.axellience.vuegwtexamples.client.examples.emitannotation.ParentEmitAnnotationComponentFactory;
import com.axellience.vuegwtexamples.client.examples.errorboundary.ErrorBoundaryComponentFactory;
import com.axellience.vuegwtexamples.client.examples.evennumbers.EvenNumbersComponentFactory;
import com.axellience.vuegwtexamples.client.examples.exclamation.ExclamationComponentFactory;
import com.axellience.vuegwtexamples.client.examples.extendjavacomponent.ChildComponentFactory;
import com.axellience.vuegwtexamples.client.examples.extendsjscomponent.ChildJavaComponentFactory;
import com.axellience.vuegwtexamples.client.examples.focus.FocusDirectiveComponentFactory;
import com.axellience.vuegwtexamples.client.examples.greet.GreetComponentFactory;
import com.axellience.vuegwtexamples.client.examples.hiwhat.HiWhatComponentFactory;
import com.axellience.vuegwtexamples.client.examples.instanciatejscomponent.FullJsWithMethodsComponentFactory;
import com.axellience.vuegwtexamples.client.examples.kitten.KittenComponentFactory;
import com.axellience.vuegwtexamples.client.examples.link.LinkComponentFactory;
import com.axellience.vuegwtexamples.client.examples.melisandre.MelisandreComponentFactory;
import com.axellience.vuegwtexamples.client.examples.message.MessageComponentFactory;
import com.axellience.vuegwtexamples.client.examples.parent.ParentComponentFactory;
import com.axellience.vuegwtexamples.client.examples.passvalues.ParentPassValuesComponentFactory;
import com.axellience.vuegwtexamples.client.examples.propdefaultvalue.ParentPropDefaultValueComponentFactory;
import com.axellience.vuegwtexamples.client.examples.recursive.RecursiveComponentFactory;
import com.axellience.vuegwtexamples.client.examples.reverse.ReverseComponentFactory;
import com.axellience.vuegwtexamples.client.examples.shareddatamodel.SharedDataModelComponentFactory;
import com.axellience.vuegwtexamples.client.examples.simplelink.SimpleLinkComponentFactory;
import com.axellience.vuegwtexamples.client.examples.simplerender.RenderAppComponentFactory;
import com.axellience.vuegwtexamples.client.examples.simpletodolist.SimpleTodoListComponentFactory;
import com.axellience.vuegwtexamples.client.examples.todolist.TodoListComponentFactory;
import com.axellience.vuegwtexamples.client.examples.todotext.TodoTextComponentFactory;
import com.axellience.vuegwtexamples.client.examples.todotextcomputed.TodoTextComputedComponentFactory;
import com.axellience.vuegwtexamples.client.examples.tree.TreeComponentFactory;
import com.axellience.vuegwtexamples.client.examples.vforonobject.VForOnObjectComponentFactory;
import com.axellience.vuegwtexamples.client.examples.vforonobjectwithkey.VForOnObjectWithKeyComponentFactory;
import com.axellience.vuegwtexamples.client.examples.vforonobjectwithkeyandindex.VForOnObjectWithKeyAndIndexComponentFactory;
import com.axellience.vuegwtexamples.client.examples.vforwithindex.VForWithIndexComponentFactory;
import com.axellience.vuegwtexamples.client.examples.vforwithrange.VForWithRangeComponentFactory;
import com.axellience.vuegwtexamples.client.examples.vonwithdomevent.VOnWithDOMEventComponentFactory;
import elemental2.dom.DomGlobal;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.JsPropertyMap;

@JsType(namespace = JsPackage.GLOBAL)
public class VueGwtExamplesService {

  public static void initExamples() {
    if (DomGlobal.document.getElementById("fullJsComponent") != null) {
      VueJsConstructor<IsVueComponent> vueClass =
          (VueJsConstructor<IsVueComponent>) ((JsPropertyMap) DomGlobal.window).get(
              "FullJsComponent");
      IsVueComponent myComponent = vueClass.instantiate();
      myComponent.vue().$mount("#fullJsComponent");
    }

    ExampleInjector exampleInjector = DaggerExampleInjector.builder().build();

    addExample("simpleLinkComponent", SimpleLinkComponentFactory.get());
    addExample("emitAnnotation", ParentEmitAnnotationComponentFactory.get());
    addExample("linkComponent", LinkComponentFactory.get());
    addExample("canHideComponent", CanHideComponentFactory.get());
    addExample("simpleTodoListComponent", SimpleTodoListComponentFactory.get());
    addExample("exclamationComponent", ExclamationComponentFactory.get());
    addExample("messageComponent", MessageComponentFactory.get());
    addExample("parentComponent", ParentComponentFactory.get());
    addExample("todoListComponent", TodoListComponentFactory.get());
    addExample("melisandreComponent", MelisandreComponentFactory.get());
    addExample("kittenComponent", KittenComponentFactory.get());
    addExample("reverseComponent", ReverseComponentFactory.get());
    addExample("vForWithIndexComponent", VForWithIndexComponentFactory.get());
    addExample("vForWithRangeComponent", VForWithRangeComponentFactory.get());
    addExample("vForOnObjectComponent", VForOnObjectComponentFactory.get());
    addExample("vForOnObjectWithKeyComponent", VForOnObjectWithKeyComponentFactory.get());
    addExample("vForOnObjectWithKeyAndIndexComponent", VForOnObjectWithKeyAndIndexComponentFactory.get());
    addExample("evenNumbersComponent", EvenNumbersComponentFactory.get());
    addExample("bindInlineStyleComponent", BindInlineStyleComponentFactory.get());
    addExample("buttonPlusOneComponent", ButtonPlusOneComponentFactory.get());
    addExample("greetComponent", GreetComponentFactory.get());
    addExample("hiWhatComponent", HiWhatComponentFactory.get());
    addExample("vOnWithDOMEventComponent", VOnWithDOMEventComponentFactory.get());
    addExample("todoTextComponent", TodoTextComponentFactory.get());
    addExample("todoTextComputedComponent", TodoTextComputedComponentFactory.get());
    addExample("sharedDataModelComponent1", SharedDataModelComponentFactory.get());
    addExample("sharedDataModelComponent2", SharedDataModelComponentFactory.get());
    addExample("sharedDataModelComponent3", SharedDataModelComponentFactory.get());
    addExample("counterWithEventComponent", CounterWithEventComponentFactory.get());
    addExample("treeComponent", TreeComponentFactory.get());
    addExample("recursiveComponent", RecursiveComponentFactory.get());
    addExample("focusDirectiveComponent", FocusDirectiveComponentFactory.get());
    addExample("renderAppComponent", RenderAppComponentFactory.get());
    addExample("extendJavaComponent", ChildComponentFactory.get());
    addExample("fullJsWithMethodsComponent", FullJsWithMethodsComponentFactory.get());
    addExample("propDefaultValueComponent", ParentPropDefaultValueComponentFactory.get());
    addExample("gotQuotesComponent", exampleInjector.gotQuoteComponentFactory());
    addExample("errorBoundary", ErrorBoundaryComponentFactory.get());
    addExample("extendJsComponent", ChildJavaComponentFactory.get());
    addExample("passValues", ParentPassValuesComponentFactory.get());
  }

  private static <T extends IsVueComponent> void addExample(String exampleId,
      VueComponentFactory<T> exampleVueComponentFactory) {
    // If we find the containing div for this example, we instantiate it
    if (DomGlobal.document.getElementById(exampleId) != null) {
      T exampleInstance = Vue.attach("#" + exampleId, exampleVueComponentFactory);
      ((JsPropertyMap) DomGlobal.window).set(exampleId, exampleInstance);
    }
  }
}
