package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.jstype.VueComponentJsTypeConstructor;
import com.axellience.vuegwt.client.jsnative.html.HTMLDocument;
import com.axellience.vuegwt.client.jsnative.html.HTMLElement;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.resources.VueGwtResources;
import com.axellience.vuegwt.client.tools.JsTools;
import com.axellience.vuegwt.client.vue.VueConstructor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Adrien Baron
 */
@JsType(namespace = JsPackage.GLOBAL)
public class VueGWT
{
    private static boolean isInjected = false;

    // Observer to know if Vue has been loaded yet
    private static boolean isVueReady = false;
    private static List<Runnable> onVueReadyCallbacks = new LinkedList<>();

    /**
     * A cache to store our {@link VueConstructor}
     */
    private static final JsObject<VueConstructor<? extends VueComponent>> componentConstructors =
        new JsObject<>();

    /**
     * Register a {@link VueConstructor} for a given {@link VueComponent} fully qualified name.
     * @param qualifiedName The fully qualified name of the {@link VueComponent} class
     * @param vueConstructor A {@link VueConstructor} you can use to instantiate components
     * @param <T> The type of the {@link VueComponent}
     */
    public static <T extends VueComponent> void register(String qualifiedName,
        VueConstructor<T> vueConstructor)
    {
        componentConstructors.set(qualifiedName, vueConstructor);
    }

    /**
     * Return the {@link VueConstructor} for the given {@link VueComponent} class.
     * @param vueComponentClass The {@link VueComponent} class
     * @param <T> The type of the {@link VueComponent}
     * @return A {@link VueConstructor} you can use to instantiate components
     */
    @JsIgnore
    public static <T extends VueComponent> VueConstructor<T> getConstructor(
        Class<T> vueComponentClass)
    {
        if (JavaScriptObject.class.equals(vueComponentClass)) {
            throw new RuntimeException("You can't use the .class of a JsComponent to instantiate it. Please use MyComponentConstructor.get() instead.");
        }
        return (VueConstructor<T>) getConstructor(vueComponentClass.getCanonicalName());
    }

    /**
     * Return the {@link VueConstructor} for the given {@link VueComponent} fully qualified name.
     * @param qualifiedName The fully qualified name of the {@link VueComponent} class
     * @return A {@link VueConstructor} you can use to instantiate components
     */
    public static VueConstructor<? extends VueComponent> getConstructor(String qualifiedName)
    {
        return componentConstructors.get(qualifiedName);
    }

    /**
     * Create a {@link Vue} instance for the given Vue Component Class.
     * You can then call $mount on it to mount the instance.
     * @param vueComponentClass The Class of the Component to create
     * @param <T> The type of the {@link VueComponent}
     * @return The created instance of our Component (not yet mounted)
     */
    public static <T extends VueComponent> T createInstance(Class<T> vueComponentClass)
    {
        return getConstructor(vueComponentClass).instantiate();
    }

    /**
     * Return the Java Constructor of our VueComponent Java Class.
     * This Constructor can be used to get the prototype of our Java Class and get the
     * VueComponent methods from it.
     * @param vueComponentClass The {@link VueComponent} we want the constructor of
     * @param <T> The type of the {@link VueComponent}
     * @return The Java constructor of our {@link VueComponent}
     */
    public static <T extends VueComponent> VueComponentJsTypeConstructor<T> getJavaConstructor(
        Class<T> vueComponentClass)
    {
        JsObject VueGWT = ((JsObject) JsTools.getWindow().get("VueGWT"));
        JsObject<VueComponentJsTypeConstructor<T>> javaComponentConstructors =
            (JsObject<VueComponentJsTypeConstructor<T>>) VueGWT.get("javaComponentConstructors");

        return javaComponentConstructors.get(vueComponentClass
            .getCanonicalName()
            .replaceAll("\\.", "_"));
    }

    /**
     * Inject scripts necessary for Vue GWT to work
     * Requires Vue to be defined in Window.
     */
    public static void inject()
    {
        if (!GWT.isClient() || isInjected)
            return;
        isInjected = true;

        HTMLDocument document = HTMLDocument.get();

        HTMLElement scriptElement = document.createElement("script");
        VueGwtResources resources = GWT.create(VueGwtResources.class);
        scriptElement.innerHTML = resources.vueGWTScript().getText();
        document.body.appendChild(scriptElement);

        if (JsTools.getWindow().get("Vue") != null)
            setVueReady();
    }

    /**
     * Should be called when Vue has been loaded
     * Is called by {@link com.axellience.vuegwt.client.resources.VueLib} when Vue is injected
     */
    public static void setVueReady()
    {
        if (isVueReady)
            return;

        // Inject VueGWT if it has not been injected yet
        VueGWT.inject();

        isVueReady = true;
        // Call on ready callbacks
        for (Runnable onReadyCbk : onVueReadyCallbacks)
            onReadyCbk.run();

        onVueReadyCallbacks.clear();
    }

    /**
     * Ask to be warned when Vue is ready.
     * If Vue is ready, the callback is called immediately.
     * @param callback The callback to call when Vue is ready.
     */
    public static void onVueReady(Runnable callback)
    {
        if (isVueReady)
        {
            callback.run();
            return;
        }

        onVueReadyCallbacks.add(callback);
    }
}
