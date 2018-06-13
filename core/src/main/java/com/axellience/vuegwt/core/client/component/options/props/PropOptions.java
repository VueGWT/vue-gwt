package com.axellience.vuegwt.core.client.component.options.props;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Definition of a property.
 *
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class PropOptions {

  public Object type;
  public boolean required;
  public Object validator;
  @JsProperty(name = "default")
  public Object defaultValue;
}
