package com.axellience.vuegwt.client.gwtpanel;

import com.axellience.vuegwt.client.Vue;
import com.axellience.vuegwt.client.component.VueComponentFactory;
import com.axellience.vuegwt.client.Vue;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Wrap a VueComponent in a GWT Panel
 * @author Adrien Baron
 */
public class VueGwtPanel<T extends Vue> extends SimplePanel
{
    private final Class<T> rootComponentClass;
    private       T        vueComponentInstance;

    public VueGwtPanel(Class<T> rootComponentClass)
    {
        super();
        this.rootComponentClass = rootComponentClass;
        VueComponentFactory<T> factory = Vue.extend(this.rootComponentClass);
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
