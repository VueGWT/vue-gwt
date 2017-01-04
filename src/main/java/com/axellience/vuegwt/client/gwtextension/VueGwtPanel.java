package com.axellience.vuegwt.client.gwtextension;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.jsnative.Vue;
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
    private final VueComponent rootComponent;

    public VueGwtPanel(VueComponent rootComponent)
    {
        super();
        this.rootComponent = rootComponent;
    }

    @Override
    protected void onAttach()
    {
        super.onAttach();
        Vue.attach(this.getElement(), this.rootComponent);
    }
}
