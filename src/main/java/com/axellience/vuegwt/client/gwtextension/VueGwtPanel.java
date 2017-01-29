package com.axellience.vuegwt.client.gwtextension;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.VueComponentFactory;
import com.axellience.vuegwt.client.jsnative.Vue;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Wrap a VueComponent in a GWT Panel
 * @author Adrien Baron
 */
public class VueGwtPanel<T extends VueComponent> extends SimplePanel
{
    private final Class<T> rootComponentClass;
    private       T        vueComponentInstance;

    public VueGwtPanel(Class<T> rootComponentClass)
    {
        super();
        this.rootComponentClass = rootComponentClass;
        VueComponentFactory factory = Vue.extend(this.rootComponentClass);
        vueComponentInstance = factory.build();
    }

    @Override
    protected void onAttach()
    {
        super.onAttach();
        vueComponentInstance.$mount(getElement());
    }

    public T vue()
    {
        return this.vueComponentInstance;
    }
}
