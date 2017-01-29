package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.jsnative.JsObject;
import com.axellience.vuegwt.client.jsnative.VueGwtTools;

/**
 * A factory for VueComponents
 * Vue.extend returns instance of this factory
 * In Vue.JS:
 * MyComponentClass = Vue.extend({});
 * instance = new MyComponentClass();
 * <p>
 * In Vue GWT:
 * myComponentFactory = Vue.extend(Component.class)
 * instance = myComponentFactory.build()
 * @author Adrien Baron
 */
public class VueComponentFactory
{
    private JsObject extendedVueClass;

    public VueComponentFactory(JsObject extendedVueClass)
    {
        this.extendedVueClass = extendedVueClass;
    }

    public <T extends VueComponent> T build()
    {
        return VueGwtTools.createInstanceForVueClass(extendedVueClass);
    }
}
