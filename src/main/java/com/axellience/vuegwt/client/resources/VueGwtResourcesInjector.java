package com.axellience.vuegwt.client.resources;

import com.axellience.vuegwt.client.jsnative.html.HTMLDocument;
import com.axellience.vuegwt.client.jsnative.html.HTMLElement;

/**
 * Inject the scripts used to manipulate JS object from the Java or convert from
 * Java representation to JS representation in the page
 * Original Source: https://github.com/ltearno/angular2-gwt/
 */
public class VueGwtResourcesInjector
{
    private static boolean injected = false;

    public static void inject()
    {
        if (injected)
            return;
        injected = true;

        HTMLDocument document = HTMLDocument.get();

        HTMLElement scriptElement = document.createElement("script");
        scriptElement.innerHTML = VueGwtResources.JS_RESOURCES.script().getText();
        document.body.appendChild(scriptElement);
    }
}