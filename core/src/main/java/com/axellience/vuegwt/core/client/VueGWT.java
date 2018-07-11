package com.axellience.vuegwt.core.client;

import com.axellience.vuegwt.core.client.observer.VueGWTObserverManager;
import com.axellience.vuegwt.core.client.observer.vuegwtobservers.CollectionObserver;
import com.axellience.vuegwt.core.client.observer.vuegwtobservers.MapObserver;
import elemental2.dom.DomGlobal;
import java.util.LinkedList;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.JsPropertyMap;

/**
 * @author Adrien Baron
 */
@JsType(namespace = JsPackage.GLOBAL)
public class VueGWT {

  private static boolean isReady = false;
  private static LinkedList<Runnable> onReadyCallbacks = new LinkedList<>();

  /**
   * Inject scripts necessary for Vue GWT to work. Also inject Vue.js library.
   */
  @JsIgnore
  public static void init() {
    if (isDevMode()) {
      VueLibDevInjector.ensureInjected();
    } else {
      VueLibInjector.ensureInjected();
    }

    // Init VueGWT
    VueGWT.initWithoutVueLib();
  }

  private static boolean isDevMode() {
    return "on".equals(System.getProperty("superdevmode", "off")) || "development".equals(System
        .getProperty("vuegwt.environment", "production"));
  }

  /**
   * Inject scripts necessary for Vue GWT to work Requires Vue to be defined in Window.
   */
  @JsIgnore
  public static void initWithoutVueLib() {
    if (!isVueLibInjected()) {
      throw new RuntimeException(
          "Couldn't find Vue.js on init. Either include it Vue.js in your index.html or call VueGWT.init() instead of initWithoutVueLib.");
    }

    // Register custom observers for Collection and Maps
    VueGWTObserverManager.get().registerVueGWTObserver(new CollectionObserver());
    VueGWTObserverManager.get().registerVueGWTObserver(new MapObserver());

    isReady = true;

    // Call on ready callbacks
    for (Runnable onReadyCbk : onReadyCallbacks) {
      onReadyCbk.run();
    }
    onReadyCallbacks.clear();
  }

  /**
   * Ask to be warned when Vue GWT is ready. If Vue GWT is ready, the callback is called
   * immediately.
   *
   * @param callback The callback to call when Vue GWT is ready.
   */
  @JsIgnore
  public static void onReady(Runnable callback) {
    if (isReady) {
      callback.run();
      return;
    }

    onReadyCallbacks.push(callback);
  }

  static boolean isVueLibInjected() {
    return ((JsPropertyMap) DomGlobal.window).get("Vue") != null;
  }
}
