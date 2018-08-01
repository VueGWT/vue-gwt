package com.axellience.vuegwt.core.client.vue;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsFunction;

/**
 * @author Adrien Baron
 */
@JsFunction
@FunctionalInterface
public interface VueWarnHandler {

  void action(String message, IsVueComponent vue, String trace);
}
