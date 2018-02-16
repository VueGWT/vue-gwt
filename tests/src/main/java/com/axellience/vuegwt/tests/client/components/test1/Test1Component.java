package com.axellience.vuegwt.tests.client.components.test1;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.VueComponent;
import jsinterop.annotations.JsProperty;

@Component
public class Test1Component extends VueComponent
{
    @JsProperty
    String frameworkName = "Vue GWT";
}
