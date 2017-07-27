package com.axellience.vuegwtexamples.client.examples.evennumbers;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.Computed;
import jsinterop.annotations.JsProperty;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EvenNumbersComponent extends VueComponent
{
    @JsProperty List<Integer> numbers;

    public EvenNumbersComponent()
    {
        this.numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    @Computed
    public List<Integer> getEvenNumbers()
    {
        return this.numbers.stream().filter(number -> number % 2 == 0).collect(Collectors.toList());
    }
}