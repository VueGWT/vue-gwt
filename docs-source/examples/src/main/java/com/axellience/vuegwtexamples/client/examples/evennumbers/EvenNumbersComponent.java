package com.axellience.vuegwtexamples.client.examples.evennumbers;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.jsnative.types.JsArray;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.Computed;
import jsinterop.annotations.JsType;

@JsType
@Component
public class EvenNumbersComponent extends VueComponent
{
    public JsArray<Integer> numbers;

    @Override
    public void created()
    {
        this.numbers = JsArray.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    @Computed
    public JsArray<Integer> getEvenNumbers()
    {
        return this.numbers.filter(number -> number % 2 == 0);
    }
}