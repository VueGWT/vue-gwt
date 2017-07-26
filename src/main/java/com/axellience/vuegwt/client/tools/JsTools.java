package com.axellience.vuegwt.client.tools;

import com.axellience.vuegwt.client.jsnative.html.HTMLDocument;
import com.axellience.vuegwt.client.jsnative.html.HTMLElement;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.resources.VueGwtResources;
import com.google.gwt.core.client.GWT;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsType;

/**
 * Scripts used to communicate between the JS world and Java
 * Source: https://github.com/ltearno/angular2-gwt/
 */
@JsType(namespace = "VueGWT", name = "jsTools")
public class JsTools
{
    @JsMethod(namespace = "console")
    public static native void log(String message);

    @JsMethod(namespace = "console")
    public static native void log(Object object);

    public static native JsObject getWindow();

    public static native boolean objectHasProperty(Object object, String property);

    public static native Object getObjectProperty(Object object, String property);

    public static native void setObjectProperty(Object object, String property, Object value);

    public static native void setObjectProperty(Object object, String property, boolean value);

    public static native void setObjectProperty(Object object, String property, byte value);

    public static native void setObjectProperty(Object object, String property, char value);

    public static native void setObjectProperty(Object object, String property, float value);

    public static native void setObjectProperty(Object object, String property, int value);

    public static native void setObjectProperty(Object object, String property, short value);

    public static native void setObjectProperty(Object object, String property, double value);

    public static native void unsetObjectProperty(Object object, String property);

    public static native <T> T getDeepValue(Object object, String path);

    public static native <T> T getArrayItem(Object array, int index);

    public static native void setArrayItem(Object array, int index, Object value);

    public static native Object call(Object method, Object thisArg, Object... args);

    @JsIgnore
    public static <T> T get(Object o, int index)
    {
        return getArrayItem(o, index);
    }

    @JsIgnore
    public static <T> void set(Object o, int index, T value)
    {
        setArrayItem(o, index, value);
    }

    @SuppressWarnings("unchecked")
    @JsIgnore
    public static <T> T get(Object o, String propertyName)
    {
        return (T) getObjectProperty(o, propertyName);
    }

    @JsIgnore
    public static void set(Object o, String propertyName, Object value)
    {
        setObjectProperty(o, propertyName, value);
    }

    // Injection
    private static boolean isInjected;

    static {
        inject();
    }

    /**
     * Inject scripts used to communicate between the JS world and Java
     */
    private static void inject()
    {
        if (!GWT.isClient() || isInjected)
            return;
        isInjected = true;

        HTMLDocument document = HTMLDocument.get();

        HTMLElement scriptElement = document.createElement("script");
        VueGwtResources resources = GWT.create(VueGwtResources.class);
        scriptElement.innerHTML = resources.jsToolsScript().getText();
        document.body.appendChild(scriptElement);
    }
}
