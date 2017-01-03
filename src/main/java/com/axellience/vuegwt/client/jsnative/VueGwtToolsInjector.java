package com.axellience.vuegwt.client.jsnative;

import com.axellience.vuegwt.client.jsnative.interop.HTMLDocument;
import com.axellience.vuegwt.client.jsnative.interop.HTMLElement;

/**
 * Inject the scripts used to manipulate JS object from the Java or convert from
 * Java representation to JS representation in the page
 * Original Source: https://github.com/ltearno/angular2-gwt/
 */
public class VueGwtToolsInjector
{
    private static boolean injected = false;

    public static void inject()
    {
        if( injected )
            return;
        injected = true;

        HTMLDocument document = HTMLDocument.get();

        HTMLElement jsToolsScriptElement = document.createElement( "script" );
        jsToolsScriptElement.innerHTML = VueGwtResources.JS_RESOURCES.jsToolsScript().getText();
        document.body.appendChild( jsToolsScriptElement );

        HTMLElement vueGwtScriptElement = document.createElement( "script" );
        vueGwtScriptElement.innerHTML = VueGwtResources.JS_RESOURCES.vueToolsScript().getText();
        document.body.appendChild( vueGwtScriptElement );
    }
}