package com.axellience.vuegwt.core.client;

import com.axellience.vuegwt.core.client.component.ComponentJavaConstructor;
import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.client.observer.VueGWTObserverManager;
import com.axellience.vuegwt.core.client.observer.vuegwtobservers.CollectionObserver;
import com.axellience.vuegwt.core.client.observer.vuegwtobservers.MapObserver;
import com.axellience.vuegwt.core.client.vue.VueFactory;
import com.axellience.vuegwt.core.client.vue.VueJsConstructor;
import com.google.gwt.core.client.JavaScriptObject;
import elemental2.dom.DomGlobal;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.JsConstructorFn;
import jsinterop.base.JsPropertyMap;

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
    private static LinkedList<Runnable> onReadyCallbacks = new LinkedList<>();

    private static final Map<String, VueFactory<? extends VueComponent>> factories =
        new HashMap<>();
    private static final Map<String, Provider<?>> factoryProviders = new HashMap<>();

    /**
     * Inject scripts necessary for Vue GWT to work.
     * Also inject Vue.js library.
     */
    @JsIgnore
    public static void init()
    {
        VueLibInjector.ensureInjected();

        // Init VueGWT
        VueGWT.initWithoutVueLib();
    }

    /**
     * Inject scripts necessary for Vue GWT to work
     * Requires Vue to be defined in Window.
     */
    @JsIgnore
    public static void initWithoutVueLib()
    {
        if (!isVueLibInjected())
            throw new RuntimeException(
                "Couldn't find Vue.js on init. Either include it Vue.js in your index.html or call VueGWT.init() instead of initWithoutVueLib.");

        // Register custom observers for Collection and Maps
        VueGWTObserverManager.get().registerVueGWTObserver(new CollectionObserver());
        VueGWTObserverManager.get().registerVueGWTObserver(new MapObserver());

        isReady = true;

        // Call on ready callbacks
        for (Runnable onReadyCbk : onReadyCallbacks)
            onReadyCbk.run();
        onReadyCallbacks.clear();
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
     * @param <T> The type of the {@link VueComponent}
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
     * Return the {@link VueJsConstructor} for the given {@link VueComponent} class.
     * @param vueComponentClass The {@link VueComponent} class
     * @param <T> The type of the {@link VueComponent}
     * @return A {@link VueJsConstructor} you can use to instantiate components
     */
    @JsIgnore
    public static <T extends VueComponent> VueJsConstructor<T> getJsConstructor(
        Class<T> vueComponentClass)
    {
        return getFactory(vueComponentClass).getJsConstructor();
    }

    /**
     * Return the {@link VueJsConstructor} for the given {@link VueComponent} fully qualified name.
     * @param qualifiedName The fully qualified name of the {@link VueComponent} class
     * @param <T> The type of the {@link VueComponent}
     * @return A {@link VueJsConstructor} you can use to instantiate components
     */
    public static <T extends VueComponent> VueJsConstructor<T> getJsConstructor(
        String qualifiedName)
    {
        return (VueJsConstructor<T>) getFactory(qualifiedName).getJsConstructor();
    }

    /**
     * Return the Java Constructor of our VueComponent Java Class.
     * This Constructor can be used to get the prototype of our Java Class and get the
     * VueComponent methods from it.
     * @param vueComponentClass The {@link VueComponent} we want the constructor of
     * @param <T> The type of the {@link VueComponent}
     * @return The Java constructor of our {@link VueComponent}
     */
    @JsIgnore
    public static <T extends VueComponent> ComponentJavaConstructor getJavaConstructor(
        Class<T> vueComponentClass)
    {
        return (ComponentJavaConstructor) JsConstructorFn.of(vueComponentClass);
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

        onReadyCallbacks.push(callback);
    }

    public static boolean isVueLibInjected()
    {
        return ((JsPropertyMap) DomGlobal.window).get("Vue") != null;
    }
}
