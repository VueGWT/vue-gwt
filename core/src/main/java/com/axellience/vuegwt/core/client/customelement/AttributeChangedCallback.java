package com.axellience.vuegwt.core.client.customelement;

import jsinterop.annotations.JsFunction;

@FunctionalInterface
@JsFunction
public interface AttributeChangedCallback
{
    void onAttributeChange(String name, Object oldValue, Object newValue);
}
