package com.axellience.vuegwt.core.client.vue;

import com.axellience.vuegwt.core.client.component.VueComponent;
import jsinterop.annotations.JsFunction;

/**
 * @author Adrien Baron
 */
@JsFunction
@FunctionalInterface
public interface VueWarnHandler
{
    void action(String message, VueComponent vue, String trace);
}
