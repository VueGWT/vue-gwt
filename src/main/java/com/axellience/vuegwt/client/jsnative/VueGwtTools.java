package com.axellience.vuegwt.client.jsnative;

import com.axellience.vuegwt.client.VueDirective;
import com.axellience.vuegwt.client.VueModel;
import jsinterop.annotations.JsMethod;

/**
 * This object provides methods used to make our Java components compatible with Vue.JS
 */
public class VueGwtTools
{
    @JsMethod(namespace = "vueGwt")
    public static native VueModel convertFromJavaToVueModel(VueModel vueModel);

    @JsMethod(namespace = "vueGwt")
    public static native VueDirective convertFromJavaToVueDirective(VueDirective vueDirective);
}
