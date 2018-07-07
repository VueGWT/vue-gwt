package com.axellience.vuegwt.core.client.component;

import com.axellience.vuegwt.core.client.vue.VueComponentFactory;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.JsPropertyMap;

/**
 * Constructor function for the ComponentExposedType. They are exposed in VueGWTComponents on
 * window
 *
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Function")
public interface ComponentExposedTypeConstructorFn<T extends IsVueComponent> {

  /**
   * Return the ExportedType Java class JS prototype.
   *
   * @return The JS prototype of the ComponentExposedType class
   */
  @JsProperty
  JsPropertyMap<Object> getPrototype();

  VueComponentFactory<T> getVueComponentFactory();
}
