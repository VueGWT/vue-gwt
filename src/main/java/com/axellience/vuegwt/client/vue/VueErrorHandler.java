package com.axellience.vuegwt.client.vue;

import com.axellience.vuegwt.client.component.VueComponent;
import elemental2.core.JsObject;
import jsinterop.annotations.JsFunction;

/**
 * @author Adrien Baron
 */
@JsFunction
@FunctionalInterface
public interface VueErrorHandler
{
    void action(JsObject err, VueComponent vue, String info);
}
