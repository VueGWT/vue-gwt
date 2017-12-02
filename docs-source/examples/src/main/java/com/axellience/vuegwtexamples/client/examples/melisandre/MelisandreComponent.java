package com.axellience.vuegwtexamples.client.examples.melisandre;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwt.core.client.component.VueComponent;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component
public class MelisandreComponent extends VueComponent
{
    @JsProperty boolean isRed = true;

    @Computed
    public MelisandreComponentStyle getMyStyle()
    {
        return MelisandreComponentClientBundle.INSTANCE.melisandreComponentStyle();
    }
}