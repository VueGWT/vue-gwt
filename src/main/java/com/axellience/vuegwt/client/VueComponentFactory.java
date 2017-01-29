package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.jsnative.JsObject;
import com.axellience.vuegwt.client.jsnative.VueGwtTools;

/**
 * @author Adrien Baron
 */
public class VueComponentFactory
{
    private JsObject extendedVueClass;

    public VueComponentFactory(JsObject extendedVueClass)
    {
        this.extendedVueClass = extendedVueClass;
    }

    public <T extends VueComponent> T build() {
        return VueGwtTools.createInstanceForVueClass(extendedVueClass);
    }
}
