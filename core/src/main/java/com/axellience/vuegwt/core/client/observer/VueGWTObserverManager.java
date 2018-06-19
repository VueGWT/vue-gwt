package com.axellience.vuegwt.core.client.observer;

import com.axellience.vuegwt.core.client.observer.functions.VueObserveArray;
import com.axellience.vuegwt.core.client.observer.functions.VueWalk;
import com.axellience.vuegwt.core.client.tools.VueGWTTools;
import elemental2.core.JsArray;
import elemental2.core.JsObject;
import elemental2.dom.DomGlobal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import jsinterop.annotations.JsMethod;
import jsinterop.base.Js;
import jsinterop.base.JsConstructorFn;
import jsinterop.base.JsPropertyMap;

/**
 * This class allow extension of the default Vue Observer. Some Java implementation in JS are not
 * reactive and therefore cannot be observed by Vue. For example the JS implementation of Collection
 * and Map. This class let you inject your own observers.
 *
 * @author Adrien Baron
 */
public class VueGWTObserverManager {

  private static VueGWTObserverManager INSTANCE;

  private final List<VueGWTObserver> observers = new LinkedList<>();
  private final Map<String, Map<String, Object>> classesPropertyCaches = new HashMap<>();
  private VueObserveArray vueObserveArrayFunction;
  private VueWalk vueWalkFunction;

  public static VueGWTObserverManager get() {
    if (INSTANCE == null) {
      VueGWTObserverManager m = new VueGWTObserverManager();
      m.captureVueObserver();
      INSTANCE = m;
    }

    return INSTANCE;
  }

  /**
   * Register a {@link VueGWTObserver}. This allow custom observation mechanism for Java Collection
   * or other object that might not be observable by the Vue observation mechanism.
   *
   * @param observer A {@link VueGWTObserver} that will be called for every object to potentially
   *        observe.
   */
  public void registerVueGWTObserver(VueGWTObserver observer) {
    observers.add(0, observer);
  }

  /**
   * Will be called from JS by the Vue observer. This is called before Vue "walk" the properties of
   * the Object to make them reactive. If your object has it's own observation mechanism, or you
   * don't want Vue to make your properties reactive (for some reason), you should return true in
   * your {@link VueGWTObserver}. You are then responsible to call notifyDep on your object
   * {@link VueObserver} and propagate observation to the object property values.
   *
   * @param object The object to potentially observe
   * @return true if we are observing and Vue shouldn't observe, false otherwise
   */
  private boolean observeJavaObject(Object object) {
    // Ignore pure JS objects, this is to avoid impacting pure Vue.js components
    if (object.getClass() == JsObject.class) {
      return false;
    }

    // Don't observe Java classes
    if (object instanceof Class) {
      return true;
    }

    // Check if we have a custom Java observer
    for (VueGWTObserver observer : observers) {
      if (observer.observe(object)) {
        return true;
      }
    }

    makeStaticallyInitializedPropertiesReactive((JsObject) object,
        object.getClass().getCanonicalName());
    return false;
  }

  /**
   * Return the Vue Observer for the given object. The object must be reactive (visible in a Vue
   * Component) otherwise this method will return null.
   *
   * @param object The object we want to get the Vue Observer from
   * @return The Vue Observer for this Object
   */
  public VueObserver getVueObserver(Object object) {
    return (VueObserver) ((JsPropertyMap) object).get("__ob__");
  }

  /**
   * Observe the given Object using Vue.js observer. Will call
   * {@link VueGWTObserverManager#observeJavaObject} to check if we have to make properties
   * reactive.
   *
   * @param object The object to observe
   */
  public void observe(Object object) {
    observeArray(new JsArray<>(object));
  }

  /**
   * Observe all the given objects. It won't automatically observe objects added/removed from the
   * array. Will call {@link VueGWTObserverManager#observeJavaObject} on each object to check if we
   * have to make its properties reactive.
   *
   * @param objects The list of object to observe
   * @param <T> The type of the JsArray
   */
  public <T> void observeArray(JsArray<T> objects) {
    vueObserveArrayFunction.observeArray(objects);
  }

  /**
   * Make all properties of the object reactive. It won't call
   * {@link VueGWTObserverManager#observeJavaObject} and will call Vue.js native walk instead. You
   * should only use this method if you are not propagating Vue.js observation but still want to
   * make some objects reactive.
   *
   * @param object The object to make reactive
   */
  public void makeReactive(Object object) {
    vueWalkFunction.walk(object);
  }

  /**
   * Customize the VueObserver instance. We get in between to be warned whenever an object is
   * observed and observe it using our Java observers if necessary.
   *
   * @param vueObserverPrototype A {@link VueObserverPrototype}
   */
  public void customizeVueObserverPrototype(VueObserverPrototype vueObserverPrototype) {
    vueObserveArrayFunction = vueObserverPrototype.observeArray;
    vueWalkFunction = vueObserverPrototype.walk;

    vueObserverPrototype.walk = (toObserve) -> {
      if (observeJavaObject(toObserve)) {
        return;
      }

      vueWalkFunction.walk(toObserve);
    };
  }

  /**
   * Capture the Vue Observer by creating a Vue Instance on the fly
   */
  private void captureVueObserver() {
    JsConstructorFn vueConstructor = ((JsPropertyMap<JsConstructorFn>) DomGlobal.window).get("Vue");
    vueConstructor.construct(new CaptureComponentDefinition());
  }

  /**
   * Due to GWT optimizations, properties on java object defined like this are not observable in
   * Vue.js when not running in dev mode: <br>
   * private String myText = "Default text"; private int myInt = 0; <br>
   * This is because GWT define the default value on the prototype and don't define it on the
   * object. Therefore Vue.js don't see those properties when initializing it's observer. To fix the
   * issue, we manually look for those properties and set them explicitly on the object.
   *
   * @param object The Java object to observe
   * @param className The Java class name to observe
   */
  private void makeStaticallyInitializedPropertiesReactive(JsObject object, String className) {
    Map<String, Object> cache = classesPropertyCaches.get(className);
    if (cache == null) {
      cache = initClassPropertiesCache(object, className);
    }

    JsPropertyMap<Object> javaObjectPropertyMap = ((JsPropertyMap<Object>) object);
    cache.forEach((key, value) -> {
      if (!object.hasOwnProperty(key)) {
        javaObjectPropertyMap.set(key, value);
      }
    });
  }

  private Map<String, Object> initClassPropertiesCache(JsObject object, String className) {
    final Map<String, Object> tmpCache = new HashMap<>();
    classesPropertyCaches.put(className, tmpCache);

    JsPropertyMap prototype = (JsPropertyMap) object.__proto__;
    prototype.forEach(property -> {
      Object value = prototype.get(property);
      if (isDefaultValue(value)) {
        tmpCache.put(property, value);
      }
    });
    return tmpCache;
  }

  private boolean isDefaultValue(Object value) {
    return Js.isTripleEqual(value, null)
        || (!"function".equals(Js.typeof(value)) && !"object".equals(Js.typeof(value)));
  }

  private static class CaptureComponentDefinition {

    @JsMethod
    public void created() {
      VueGWTObserverManager.get()
          .customizeVueObserverPrototype(VueGWTTools.getDeepValue(this, "$data.__ob__.__proto__"));
    }
  }
}
