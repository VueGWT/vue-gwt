package com.axellience.vuegwt.client.component;

import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import jsinterop.annotations.JsMethod;

/**
 * @author Adrien Baron
 */
public interface HasCustomizeOptions
{
    @JsMethod
    void customizeOptions(VueComponentOptions options);
}
