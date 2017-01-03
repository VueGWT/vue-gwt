package com.axellience.vuegwt.client.gwtextension;

import com.axellience.vuegwt.client.VueApp;
import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.jsnative.Vue;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Wrap a VueComponent in a GWT Panel
 * For that it creates a custom VueApp instance that just instantiate this VueComponent in it's root.
 * Your component doesn't have to be registered globally for this to work
 *
 * /!\ Your VueComponent MUST use kebab case in it's name for this to work
 */
public class VueGwtPanel extends SimplePanel
{
    class GwtWidgetVueApp extends VueApp
    {
        public void init(Element element, VueComponent rootComponent) {
            this.init(element);
            this.registerComponent(rootComponent);
        }
    }

    private GwtWidgetVueApp rootVueApp;
    private final VueComponent rootComponent;

    public VueGwtPanel(VueComponent rootComponent)
    {
        super();
        this.rootComponent = rootComponent;
        rootVueApp = new GwtWidgetVueApp();
        this.getElement().appendChild(DOM.createElement(rootComponent.getName()));
    }

    @Override
    protected void onAttach()
    {
        super.onAttach();
        rootVueApp.init(this.getElement(), rootComponent);

        // Bootstrap Local VueApp
        Vue.app(rootVueApp);
    }
}
