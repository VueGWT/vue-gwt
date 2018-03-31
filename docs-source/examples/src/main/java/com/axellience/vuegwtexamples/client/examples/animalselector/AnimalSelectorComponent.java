package com.axellience.vuegwtexamples.client.examples.animalselector;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import elemental2.dom.Event;
import elemental2.dom.HTMLInputElement;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

@Component
public class AnimalSelectorComponent implements IsVueComponent
{
    @Prop
    @JsProperty
    String userName;

    @JsMethod
    public void selectAnimal(Event event)
    {
        asVue().$emit("animal-selected", ((HTMLInputElement) event.target).value);
    }
}
