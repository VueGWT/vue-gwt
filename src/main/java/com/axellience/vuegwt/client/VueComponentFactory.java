package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.jsnative.types.JsObject;
import com.axellience.vuegwt.client.jsnative.VueGwtTools;

/**
 * A factory for VueComponents
 * Vue.extend returns instance of this factory
 * In Vue.js:
 * MyComponentClass = Vue.extend({});
 * instance = new MyComponentClass();
 * <p>
 * In Vue GWT:
 * myComponentFactory = Vue.extend(Component.class)
 * instance = myComponentFactory.build()
 * @author Adrien Baron
 */
public class VueComponentFactory<T extends VueComponent>
{
    private JsObject extendedVueClass;

    public VueComponentFactory(JsObject extendedVueClass)
    {
        this.extendedVueClass = extendedVueClass;
    }

    public T build()
    {
        return VueGwtTools.createInstanceForVueClass(extendedVueClass);
    }
}
