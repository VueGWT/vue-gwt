package com.axellience.vuegwt.client.vue;

import elemental2.core.Array;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.JsPropertyMap;

/**
 * @author Adrien Baron
 */
@JsType(isNative = true)
public class VueConfig
{
    @JsProperty protected boolean silent;
    @JsProperty protected JsPropertyMap optionMergeStrategies;
    @JsProperty protected boolean devtools;
    @JsProperty protected boolean productionTip;
    @JsProperty protected boolean performance;
    @JsProperty protected VueErrorHandler errorHandler;
    @JsProperty protected VueWarnHandler warnHandler;
    @JsProperty protected Array<String> ignoredElements;
    @JsProperty protected JsPropertyMap keyCodes;

    @JsOverlay
    public final boolean isSilent()
    {
        return silent;
    }

    @JsOverlay
    public final VueConfig setSilent(boolean silent)
    {
        this.silent = silent;
        return this;
    }

    @JsOverlay
    public final JsPropertyMap getOptionMergeStrategies()
    {
        return optionMergeStrategies;
    }

    @JsOverlay
    public final VueConfig setOptionMergeStrategies(JsPropertyMap optionMergeStrategies)
    {
        this.optionMergeStrategies = optionMergeStrategies;
        return this;
    }

    @JsOverlay
    public final boolean isDevtools()
    {
        return devtools;
    }

    @JsOverlay
    public final VueConfig setDevtools(boolean devtools)
    {
        this.devtools = devtools;
        return this;
    }

    @JsOverlay
    public final boolean isProductionTip()
    {
        return productionTip;
    }

    @JsOverlay
    public final VueConfig setProductionTip(boolean productionTip)
    {
        this.productionTip = productionTip;
        return this;
    }

    @JsOverlay
    public final boolean isPerformance()
    {
        return performance;
    }

    @JsOverlay
    public final VueConfig setPerformance(boolean performance)
    {
        this.performance = performance;
        return this;
    }

    @JsOverlay
    public final VueErrorHandler getErrorHandler()
    {
        return errorHandler;
    }

    @JsOverlay
    public final VueConfig setErrorHandler(VueErrorHandler errorHandler)
    {
        this.errorHandler = errorHandler;
        return this;
    }

    @JsOverlay
    public final VueWarnHandler getWarnHandler()
    {
        return warnHandler;
    }

    @JsOverlay
    public final VueConfig setWarnHandler(VueWarnHandler warnHandler)
    {
        this.warnHandler = warnHandler;
        return this;
    }

    @JsOverlay
    public final Array<String> getIgnoredElements()
    {
        return ignoredElements;
    }

    @JsOverlay
    public final VueConfig setIgnoredElements(Array<String> ignoredElements)
    {
        this.ignoredElements = ignoredElements;
        return this;
    }

    @JsOverlay
    public final VueConfig addIgnoredElement(String ignoredElement)
    {
        if (this.ignoredElements == null)
            this.ignoredElements = new Array<>();

        this.ignoredElements.push(ignoredElement);
        return this;
    }

    @JsOverlay
    public final JsPropertyMap getKeyCodes()
    {
        return keyCodes;
    }

    @JsOverlay
    public final VueConfig setKeyCodes(JsPropertyMap keyCodes)
    {
        this.keyCodes = keyCodes;
        return this;
    }

    @JsOverlay
    public final VueConfig setKeyCode(String key, int value)
    {
        if (this.keyCodes == null)
            this.keyCodes = JsPropertyMap.of();

        this.keyCodes.set(key, value);
        return this;
    }
}
