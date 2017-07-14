package com.axellience.vuegwtexamples.client.examples.melisandre;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
@Component
public class MelisandreComponent extends VueComponent
{
    public boolean isRed;

    @Override
    public void created() {
        this.isRed = true;
    }
}