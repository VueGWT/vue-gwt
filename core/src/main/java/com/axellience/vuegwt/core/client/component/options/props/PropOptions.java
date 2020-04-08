package com.axellience.vuegwt.core.client.component.options.props;

import elemental2.core.Function;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Any;

/**
 * Definition of a property.
 *
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class PropOptions {

  public Object type;
  public boolean required;
  public Function validator;
  @JsProperty(name = "default")
  public Any defaultValue;
}
