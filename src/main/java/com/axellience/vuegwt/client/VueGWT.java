package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.jsnative.html.HTMLDocument;
import com.axellience.vuegwt.client.jsnative.html.HTMLElement;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.resources.VueGwtResources;
import com.axellience.vuegwt.client.vue.VueConstructor;
import com.google.gwt.core.client.GWT;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType(namespace = JsPackage.GLOBAL)
public class VueGWT
{
    private static boolean isInjected = false;

    private static final JsObject<VueConstructor<? extends VueComponent>> componentConstructors =
        new JsObject<>();

    public static <T extends VueComponent> void register(String qualifiedName,
        VueConstructor<T> vueConstructor)
    {
        componentConstructors.set(qualifiedName, vueConstructor);
    }

    public static VueConstructor<? extends VueComponent> get(String qualifiedName)
    {
        return componentConstructors.get(qualifiedName);
    }

    /**
     * Inject the scripts used to manipulate JS object from the Java or convert from
     * Java representation to JS representation in the page
     */
    public static void inject()
    {
        if (!GWT.isClient() || isInjected)
            return;
        isInjected = true;

        HTMLDocument document = HTMLDocument.get();

        HTMLElement scriptElement = document.createElement("script");
        VueGwtResources resources = GWT.create(VueGwtResources.class);
        scriptElement.innerHTML = resources.script().getText();
        document.body.appendChild(scriptElement);
    }
}
