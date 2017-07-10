package com.axellience.vuegwt.client.jsnative.vue;

import com.axellience.vuegwt.client.jsnative.types.JsArray;
import com.axellience.vuegwt.client.jsnative.types.JsObject;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
public final class VueConfig
{
    public boolean silent;

    public JsObject optionMergeStrategies;

    public boolean devtools;

    public VueErrorHandler errorHandler;

    public JsArray<String> ignoredElements;

    public JsObject keyCodes;

    public boolean performance;

    public boolean productionTip;
}
