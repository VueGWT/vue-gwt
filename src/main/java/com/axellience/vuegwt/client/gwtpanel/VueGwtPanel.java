package com.axellience.vuegwt.client.gwtpanel;

import com.axellience.vuegwt.client.VueGWT;
import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.vue.VueFactory;
import com.axellience.vuegwt.client.vue.VueJsConstructor;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Wrap a {@link VueComponent} instance in a GWT Panel
 */
public class VueGwtPanel<T extends VueComponent> extends Widget
{
    private final T vueComponentInstance;

    public VueGwtPanel(VueFactory<T> vueFactory)
    {
        this(vueFactory.getJsConstructor());
    }

    public VueGwtPanel(VueJsConstructor<T> vueJsConstructor)
    {
        super();
        setElement(Document.get().createDivElement());
        vueComponentInstance = vueJsConstructor.instantiate();
    }

    public VueGwtPanel(Class<T> vueClass)
    {
        this(VueGWT.getFactory(vueClass));
    }
    
    public T vue()
    {
        return vueComponentInstance;
    }

    @Override
    protected void onLoad()
    {
        super.onLoad();
        if (!isMounted()) {
            mountVueComponent();
        }
    }

    private boolean isMounted()
    {
        return getElement().hasChildNodes();
    }

    private void mountVueComponent()
    {
        Element vueElement = Document.get().createDivElement();
        getElement().appendChild(vueElement);
        vueComponentInstance.$mount(vueElement);
    }
}
