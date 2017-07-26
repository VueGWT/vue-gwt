package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.jsnative.html.HTMLDocument;
import com.axellience.vuegwt.client.jsnative.html.HTMLElement;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.resources.VueGwtResources;
import com.axellience.vuegwt.client.resources.VueLibResources;
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
    private static boolean isVueLibInjected = false;
    private static boolean isInjected = false;

    private static final JsObject<VueConstructor<? extends Vue>> componentConstructors =
        new JsObject<>();

    public static <T extends Vue> void register(String qualifiedName,
        VueConstructor<T> vueConstructor)
    {
        componentConstructors.set(qualifiedName, vueConstructor);
    }

    public static VueConstructor<? extends Vue> get(String qualifiedName)
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
        scriptElement.innerHTML = VueGwtResources.JS_RESOURCES.script().getText();
        document.body.appendChild(scriptElement);
    }

    public static void injectVueLib()
    {
        if (!GWT.isClient() || isVueLibInjected)
            return;
        isVueLibInjected = true;

        HTMLDocument document = HTMLDocument.get();

        HTMLElement scriptElement = document.createElement("script");
        scriptElement.innerHTML = VueLibResources.LIB_RESOURCES.vueScript().getText();
        document.body.appendChild(scriptElement);
    }
}
