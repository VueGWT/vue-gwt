package com.axellience.vuegwt.core.client.observer;

import static jsinterop.base.Js.uncheckedCast;

import com.axellience.vuegwt.core.client.Vue;
import com.axellience.vuegwt.core.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.core.client.tools.VueGWTTools;
import com.axellience.vuegwt.core.client.vue.VueJsConstructor;
import elemental2.core.Function;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.JsPropertyMap;

class VueJsObserverGetter {

  @JsProperty
  private Proto __proto__;

  VueObserver getVueJsObserver() {
    VueComponentOptions options = new VueComponentOptions();

    try {
      options.setComponentExportedTypePrototype(this.__proto__);
      options.addMethod("getObserver", this.__proto__.getObserver);
    } catch (Exception e) {
      throw new IllegalStateException(
          "Cannot initialize VueJsObserver. Please be sure to add `-generateJsInteropExports` flag to your GWT configuration.",
          e);
    }

    VueJsConstructor vueJsConstructor = Vue.extendJavaComponent(options);
    VueJsObserverGetter getter = uncheckedCast(vueJsConstructor.instantiate());
    return getter.getObserver();
  }

  @JsMethod
  private VueObserver getObserver() {
    return VueGWTTools.getDeepValue(this, "$data.__ob__");
  }

  @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
  private static class Proto implements JsPropertyMap<Object> {

    public Function getObserver;
  }
}
