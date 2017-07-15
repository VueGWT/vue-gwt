package com.axellience.vuegwt.client.gwtpanel;

import com.axellience.vuegwt.client.Vue;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Wrap a VueComponent in a GWT Panel
 * @author Adrien Baron
 */
public class VueGwtPanel<T extends Vue> extends SimplePanel
{
    private T vueComponentInstance;

    public VueGwtPanel(Class<T> rootComponentClass)
    {
        super();
        vueComponentInstance = Vue.getJsVueClass(rootComponentClass).instantiate();
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
