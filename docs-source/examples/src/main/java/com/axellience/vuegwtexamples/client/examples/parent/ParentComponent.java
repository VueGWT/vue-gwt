package com.axellience.vuegwtexamples.client.examples.parent;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsType;

@JsType
@Component(components = { TodoComponent.class })
public class ParentComponent extends VueComponent
{
}