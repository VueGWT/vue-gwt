package com.axellience.vuegwt.client.vue;

import com.axellience.vuegwt.client.Vue;
import jsinterop.annotations.JsFunction;

/**
 * @author Adrien Baron
 */
@JsFunction
@FunctionalInterface
public interface VueWarnHandler
{
    void action(String message, Vue vue, String trace);
}
