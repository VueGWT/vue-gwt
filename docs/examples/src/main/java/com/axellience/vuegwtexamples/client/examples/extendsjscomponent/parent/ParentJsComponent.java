package com.axellience.vuegwtexamples.client.examples.extendsjscomponent.parent;

import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.annotations.component.JsComponent;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;


/**
 * @author Adrien Baron
 */
@JsComponent("ParentJsComponent")
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Function")
public class ParentJsComponent implements IsVueComponent {

  @Data
  public String parentMessage;

  public native int parentMultiplyBy2(int value);

  @Computed
  public native String getParentComputed();
}
