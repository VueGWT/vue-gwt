package com.axellience.vuegwt.client.resources;

import com.axellience.vuegwt.client.jsnative.html.HTMLDocument;
import com.axellience.vuegwt.client.jsnative.html.HTMLElement;
import com.google.gwt.core.client.GWT;

/**
 * @author Adrien Baron
 */
public class VueLib
{
    private static boolean isVueLibInjected = false;

    public static void inject()
    {
        if (!GWT.isClient() || isVueLibInjected)
            return;
        isVueLibInjected = true;

        HTMLDocument document = HTMLDocument.get();

        HTMLElement scriptElement = document.createElement("script");
        VueLibResources resources = GWT.create(VueLibResources.class);
        scriptElement.innerHTML = resources.vueScript().getText();
        document.body.appendChild(scriptElement);
    }
}
