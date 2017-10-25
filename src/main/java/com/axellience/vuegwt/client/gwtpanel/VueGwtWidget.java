package com.axellience.vuegwt.client.gwtpanel;

import com.axellience.vuegwt.client.VueGWT;
import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.vue.VueFactory;
import com.axellience.vuegwt.client.vue.VueJsConstructor;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import jsinterop.base.Js;

/**
 * Wraps a {@link VueComponent} instance in a GWT Widget.
 */
public class VueGwtWidget<T extends VueComponent> extends Widget
{
    private final T vueComponentInstance;

    public VueGwtWidget(VueFactory<T> vueFactory)
    {
        this(vueFactory.getJsConstructor());
    }

    public VueGwtWidget(VueJsConstructor<T> vueJsConstructor)
    {
        super();
        setElement(Document.get().createDivElement());
        vueComponentInstance = vueJsConstructor.instantiate();
    }

    public VueGwtWidget(Class<T> vueClass)
    {
        this(VueGWT.getFactory(vueClass));
    }

    /**
     * Returns the instance of the vue component wrapped in this widget.
     * 
     * @return the component instance.
     */
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
        HTMLDivElement vueElement = (HTMLDivElement) DomGlobal.document.createElement("div");
        getElement().appendChild(Js.cast(vueElement));
        vueComponentInstance.$mount(vueElement);
    }
}
