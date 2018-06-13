package com.axellience.vuegwtexamples.client;

import com.axellience.vuegwt.core.client.Vue;
import com.axellience.vuegwt.core.client.VueGWT;
import com.axellience.vuegwtexamples.client.examples.animalselector.AnimalSelectorComponent;
import com.axellience.vuegwtexamples.client.examples.melisandre.MelisandreComponentClientBundle;
import com.google.gwt.core.client.EntryPoint;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLScriptElement;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class VueGwtExamplesApp implements EntryPoint {

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    VueGWT.init();

    // Inject JS Components
    HTMLScriptElement scriptElement =
        (HTMLScriptElement) DomGlobal.document.createElement("script");
    scriptElement.text = JsFilesResources.INSTANCE.jsComponents().getText();
    DomGlobal.document.body.appendChild(scriptElement);

    MelisandreComponentClientBundle.INSTANCE.melisandreComponentStyle().ensureInjected();
    Vue.customElement("animal-selector", AnimalSelectorComponent.class);

    VueGwtExamplesService.initExamples();
  }
}
