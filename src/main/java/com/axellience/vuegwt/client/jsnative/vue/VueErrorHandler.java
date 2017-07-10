package com.axellience.vuegwt.client.jsnative.vue;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.jsnative.types.JsObject;
import jsinterop.annotations.JsFunction;

/**
 * @author Adrien Baron
 */
@JsFunction
@FunctionalInterface
public interface VueErrorHandler
{
    void action(JsObject err, VueComponent vue, JsObject info);
}
