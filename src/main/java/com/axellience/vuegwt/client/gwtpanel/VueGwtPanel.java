package com.axellience.vuegwt.client.gwtpanel;

import com.axellience.vuegwt.client.VueGWT;
import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.vue.VueFactory;
import com.axellience.vuegwt.client.vue.VueJsConstructor;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Wrap a {@link VueComponent} instance in a GWT Panel
 * @author Adrien Baron
 */
public class VueGwtPanel<T extends VueComponent> extends SimplePanel
{
    private T vueComponentInstance;

    public VueGwtPanel(VueFactory<T> vueFactory)
    {
        this(vueFactory.getJsConstructor());
    }

    public VueGwtPanel(VueJsConstructor<T> vueJsConstructor)
    {
        super();
        vueComponentInstance = vueJsConstructor.instantiate();
    }

    public VueGwtPanel(Class<T> vueClass)
    {
        this(VueGWT.getFactory(vueClass));
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
