package com.axellience.vuegwt.tests.client;

import com.axellience.vuegwt.core.client.VueGWT;
import com.google.gwt.core.client.EntryPoint;
import elemental2.core.Function;
import elemental2.core.JsArray;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

public class VueGwtTestsApp implements EntryPoint {

  public void onModuleLoad() {
    VueGWT.init();
    Window.onVueGwtTestsReady.forEach(Function::call);
  }

  @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "window")
  private static class Window {

    static JsArray<Function> onVueGwtTestsReady;
  }
}
