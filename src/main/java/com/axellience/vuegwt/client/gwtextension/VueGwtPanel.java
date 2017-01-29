package com.axellience.vuegwt.client.gwtextension;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.jsnative.Vue;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Wrap a VueComponent in a GWT Panel
 * For that it creates a custom VueApp instance that just instantiate this VueComponent in it's
 * root.
 * Your component doesn't have to be registered globally for this to work
 * <p>
 * /!\ Your VueComponent MUST use kebab case in it's name for this to work
 */
public class VueGwtPanel<T extends VueComponent> extends SimplePanel
{
    private final Class<T> rootComponentClass;
    private       T                  vueComponentInstance;

    public VueGwtPanel(Class<T> rootComponentClass)
    {
        super();
        this.rootComponentClass = rootComponentClass;
    }

    @Override
    protected void onAttach()
    {
        super.onAttach();
        this.vueComponentInstance = Vue.attach(this.getElement(), this.rootComponentClass);
    }

    public T vue()
    {
        return this.vueComponentInstance;
    }
}
