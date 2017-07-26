package com.axellience.vuegwt.client.observer;

import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.observer.vuegwtobservers.CollectionObserver;
import com.axellience.vuegwt.client.observer.vuegwtobservers.MapObserver;
import jsinterop.annotations.JsMethod;

import java.util.LinkedList;
import java.util.List;

/**
 * This class allow extension of the default Vue Observer.
 * Some Java implementation in JS are not reactive and therefore cannot be observed by Vue.
 * For example the JS implementation of Collection and Map.
 * This class let you inject your own observers.
 * @author Adrien Baron
 */
public class VueGWTObserverManager
{
    private static List<VueGWTObserver> observers = new LinkedList<>();

    static
    {
        registerVueGWTObserver(new CollectionObserver());
        registerVueGWTObserver(new MapObserver());
    }

    /**
     * Register a {@link VueGWTObserver}.
     * This allow custom observation mechanism for Java Collection or other object that might
     * not be observable by the Vue observation mechanism.
     * @param observer A {@link VueGWTObserver} that will be called for every object to potentially
     * observe.
     */
    public static void registerVueGWTObserver(VueGWTObserver observer)
    {
        observers.add(observer);
    }

    /**
     * Return the Vue Observer for the given object.
     * The object must be reactive (visible in a Vue Component) otherwise this method
     * will return null.
     * @param object The object we want to get the Vue Observer from
     * @return The Vue Observer for this Object
     */
    public static VueObserver getVueObserver(Object object)
    {
        return (VueObserver) ((JsObject) object).get("__ob__");
    }

    /**
     * Will be called from JS by the Vue observer.
     * This is called before Vue "walk" the properties of the Object to make them reactive.
     * If your object has it's own observation mechanism, or you don't want Vue to make your
     * properties reactive (for some reason), you should return true in your {@link VueGWTObserver}.
     * You are then responsible to call notifyDep on your object {@link VueObserver} and propagate
     * observation to the object property values.
     * @param object The object to potentially observe
     * @return true if we are observing and Vue shouldn't observe, false otherwise
     */
    @JsMethod(namespace = "VueGWTObserverManager")
    private static boolean observeJavaObject(Object object)
    {
        if (isJavaObserved(object))
            return true;

        for (VueGWTObserver observer : observers)
        {
            if (observer.observe(object))
            {
                setIsJavaObserved(object);
                return true;
            }
        }

        return false;
    }

    /**
     * Return true if the object is already observed by Java
     * @param object The object potentially already observed
     * @return true if the object is already observed, false otherwise
     */
    private static boolean isJavaObserved(Object object)
    {
        Boolean isObserved = (Boolean) ((JsObject) object).get("$vueGwtIsJavaObserved");
        return isObserved != null && isObserved;
    }

    /**
     * Set a flag to say our object is observed by Java
     * @param object The object we are observing
     */
    private static void setIsJavaObserved(Object object)
    {
        ((JsObject) object).set("$vueGwtIsJavaObserved", true);
    }
}
