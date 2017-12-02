package com.axellience.vuegwt.core.client.customelement;

import com.axellience.vuegwt.core.client.component.VueComponent;
import jsinterop.annotations.JsFunction;

@FunctionalInterface
@JsFunction
public interface AttributeChangedCallback<T extends VueComponent>
{
    void onAttributeChange(VueCustomElement<T> element, String name, Object oldValue, Object newValue);
}
