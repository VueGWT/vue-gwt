package com.axellience.vuegwt.client.gwtpanel;

import com.axellience.vuegwt.client.Vue;
import com.axellience.vuegwt.client.vue.VueConstructor;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Wrap a {@link Vue} instance in a GWT Panel
 * @author Adrien Baron
 */
public class VueGwtPanel<T extends Vue> extends SimplePanel
{
    private T vueComponentInstance;

    public VueGwtPanel(VueConstructor<T> vueConstructor)
    {
        super();
        vueComponentInstance = vueConstructor.instantiate();
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
