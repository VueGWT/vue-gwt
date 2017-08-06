package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.jsnative.html.HTMLDocument;
import com.axellience.vuegwt.client.jsnative.html.HTMLElement;
import com.axellience.vuegwt.client.jsnative.jsfunctions.JsRunnable;
import com.axellience.vuegwt.client.jsnative.jstypes.JsArray;
import com.axellience.vuegwt.client.resources.VueGwtResources;
import com.axellience.vuegwt.client.resources.VueLibResources;
import com.axellience.vuegwt.client.tools.JsTools;
import com.axellience.vuegwt.client.vue.VueFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import javax.inject.Provider;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Adrien Baron
 */
@JsType(namespace = JsPackage.GLOBAL)
public class VueGWT
{
    private static boolean isReady = false;
    private static JsArray<JsRunnable> onReadyCallbacks = new JsArray<>();
    private static LinkedList<Runnable> onReadyCallbacksJava = new LinkedList<>();

    private static final Map<String, VueFactory<? extends VueComponent>> factories =
        new HashMap<>();
    private static final Map<String, Provider<?>> factoryProviders = new HashMap<>();

    /**
     * Inject scripts necessary for Vue GWT to work.
     * Also inject Vue.js library.
     */
    @JsIgnore
    public static void initWithVueLib()
    {
        if (!isVueLibInjected())
        {
            HTMLDocument document = HTMLDocument.get();

            HTMLElement scriptElement = document.createElement("script");
            VueLibResources resources = GWT.create(VueLibResources.class);
            scriptElement.innerHTML = resources.vueScript().getText();
            document.body.appendChild(scriptElement);
        }

        // Init VueGWT
        VueGWT.init();
    }

    /**
     * Inject scripts necessary for Vue GWT to work
     * Requires Vue to be defined in Window.
     */
    @JsIgnore
    public static void init()
    {
        if (!isVueLibInjected())
            throw new RuntimeException(
                "Couldn't find Vue.js on init. Either include it in your index.html or call VueGWT.initWithVueLib() instead.");

        HTMLDocument document = HTMLDocument.get();

        HTMLElement scriptElement = document.createElement("script");
        VueGwtResources resources = GWT.create(VueGwtResources.class);
        scriptElement.innerHTML = resources.vueGWTScript().getText();
        document.body.appendChild(scriptElement);

        isReady = true;

        // Call on ready callbacks
        for (JsRunnable onReadyCbk : onReadyCallbacks.iterate())
            onReadyCbk.run();
        onReadyCallbacks.length = 0;

        for (Runnable onReadyCbk : onReadyCallbacksJava)
            onReadyCbk.run();
        onReadyCallbacksJava.clear();
    }

    /**
     * Create a {@link Vue} instance for the given Vue Component Class.
     * You can then call $mount on it to mount the instance.
     * @param vueComponentClass The Class of the Component to create
     * @param <T> The type of the {@link VueComponent}
     * @return The created instance of our Component (not yet mounted)
     */
    @JsIgnore
    public static <T extends VueComponent> T createInstance(Class<T> vueComponentClass)
    {
        return getFactory(vueComponentClass).create();
    }

    /**
     * Return the {@link VueFactory} for the given {@link VueComponent} class.
     * @param vueComponentClass The {@link VueComponent} class
     * @param <T> The type of the {@link VueComponent}
     * @return A {@link VueFactory} you can use to instantiate components
     */
    @JsIgnore
    public static <T extends VueComponent> VueFactory<T> getFactory(Class<T> vueComponentClass)
    {
        if (JavaScriptObject.class.equals(vueComponentClass))
        {
            throw new RuntimeException(
                "You can't use the .class of a JsComponent to instantiate it. Please use MyComponentFactory.get() instead.");
        }
        return (VueFactory<T>) getFactory(vueComponentClass.getCanonicalName());
    }

    /**
     * Return the {@link VueFactory} for the given {@link VueComponent} fully qualified name.
     * @param qualifiedName The fully qualified name of the {@link VueComponent} class
     * @return A {@link VueFactory} you can use to instantiate components
     */
    public static <T extends VueComponent> VueFactory<T> getFactory(String qualifiedName)
    {
        if (factoryProviders.containsKey(qualifiedName))
            return (VueFactory<T>) factoryProviders.get(qualifiedName).get();

        if (factories.containsKey(qualifiedName))
            return (VueFactory<T>) factories.get(qualifiedName);

        throw new RuntimeException("Couldn't find VueFactory for Component: "
            + qualifiedName
            + ". Make sure that annotation are being processed, and that you added the -generateJsInteropExports flag to GWT. You can also try a \"mvn clean\" on your maven project.");
    }

    /**
     * Register a {@link VueFactory} for a given {@link VueComponent} fully qualified name.
     * @param qualifiedName The fully qualified name of the {@link VueComponent} class
     * @param vueFactory A {@link VueFactory} you can use to instantiate components
     * @param <T> The type of the {@link VueComponent}
     */
    @JsIgnore
    public static <T extends VueComponent> void register(String qualifiedName,
        VueFactory<T> vueFactory)
    {
        factories.put(qualifiedName, vueFactory);
    }

    /**
     * Register a {@link Supplier} returning the {@link VueFactory} for a given {@link
     * VueComponent}.
     * @param qualifiedName The fully qualified name of the {@link VueComponent} class
     * @param vueFactoryProvider A static {@link Provider} which provides {@link VueFactory} that
     * you can use to instantiate components
     */
    @JsIgnore
    public static void register(String qualifiedName, Provider<?> vueFactoryProvider)
    {
        factoryProviders.put(qualifiedName, vueFactoryProvider);
    }

    /**
     * Ask to be warned when Vue GWT is ready.
     * If Vue GWT is ready, the callback is called immediately.
     * @param callback The callback to call when Vue GWT is ready.
     */
    @JsIgnore
    public static void onReady(Runnable callback)
    {
        if (isReady)
        {
            callback.run();
            return;
        }

        onReadyCallbacksJava.push(callback);
    }

    private static boolean isVueLibInjected()
    {
        return JsTools.getWindow().get("Vue") != null;
    }
}
