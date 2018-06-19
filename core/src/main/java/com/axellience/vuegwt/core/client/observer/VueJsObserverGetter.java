package com.axellience.vuegwt.core.client.observer;

import static jsinterop.base.Js.uncheckedCast;

import com.axellience.vuegwt.core.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.core.client.tools.VueGWTTools;
import elemental2.core.Function;
import elemental2.dom.DomGlobal;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.JsConstructorFn;
import jsinterop.base.JsPropertyMap;

class VueJsObserverGetter {

  @JsProperty
  private Proto __proto__;

  VueObserverPrototype getVueJsObserverPrototype() {
    JsConstructorFn vueConstructor = ((JsPropertyMap<JsConstructorFn>) DomGlobal.window).get("Vue");
    VueComponentOptions options = new VueComponentOptions();
    options.addMethod("getObserverPrototype", this.__proto__.getObserverPrototype);

    VueJsObserverGetter getter = uncheckedCast(vueConstructor.construct(options));
    return getter.getObserverPrototype();
  }

  @JsMethod
  private VueObserverPrototype getObserverPrototype() {
    return VueGWTTools.getDeepValue(this, "$data.__ob__.__proto__");
  }

  @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
  private static class Proto {

    public Function getObserverPrototype;
  }
}
