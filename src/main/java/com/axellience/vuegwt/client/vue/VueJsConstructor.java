package com.axellience.vuegwt.client.vue;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.client.jsnative.jstypes.JsFunction;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.tools.JsTools;
import com.axellience.vuegwt.client.tools.VueGWTTools;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.JsComponent;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * A Java representation of a Vue.js Constructor.
 * Vue.js Constructor are JavaScript Function obtained when calling VueComponent.extend().
 * All the {@link Component} and {@link JsComponent} get a generated {@link VueFactory} that wraps a
 * {@link VueJsConstructor}.
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Function")
public class VueJsConstructor<T extends VueComponent> extends JsFunction
{
    @JsOverlay
    public final T instantiate()
    {
        return VueGWTTools.createInstanceForVueClass(this);
    }

    @JsOverlay
    public final <K extends T> VueJsConstructor<K> extend(
        VueComponentOptions<K> vueComponentOptions)
    {
        return VueGWTTools.extendVueClass(this, vueComponentOptions);
    }

    @JsOverlay
    public final <K extends T> VueJsConstructor<K> extendJavaComponent(
        VueComponentOptions<K> componentOptions)
    {
        componentOptions.addAllProviders(getOptions().getProviders());
        VueJsConstructor<K> extendedVueJsConstructor = extend(componentOptions);
        VueGWTTools.extendVueConstructorWithJavaPrototype(extendedVueJsConstructor,
            componentOptions.getComponentJavaPrototype());

        return extendedVueJsConstructor;
    }

    @JsOverlay
    public final VueComponentOptions<T> getOptions()
    {
        return JsTools.get(this, "options");
    }

    @JsOverlay
    public final JsObject getOptionsComponents()
    {
        JsObject options = getOptions();
        JsObject components = (JsObject) options.get("components");
        if (components == null)
        {
            components = new JsObject();
            options.set("components", components);
        }
        return components;
    }

    @JsOverlay
    public final JsObject getOptionsDirectives()
    {
        JsObject options = JsTools.get(this, "options");
        JsObject directives = (JsObject) options.get("directives");
        if (directives == null)
        {
            directives = new JsObject();
            options.set("directives", directives);
        }
        return directives;
    }
}
