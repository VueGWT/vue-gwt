package com.axellience.vuegwt.core.client.component.options.computed;

import elemental2.core.Function;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * Definition of a Computed property
 *
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class ComputedOptions {

  public Function get;
  public Function set;
}
