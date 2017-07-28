package com.axellience.vuegwt.client.vue;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
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
