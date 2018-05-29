package com.axellience.vuegwt.core.client.vue;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.core.client.directive.options.VueDirectiveOptions;
import com.axellience.vuegwt.core.client.tools.VueGWTTools;
import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.JsComponent;
import elemental2.core.JsObject;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.JsConstructorFn;
import jsinterop.base.JsPropertyMap;

/**
 * A Java representation of a Vue.js Constructor.
 * Vue.js Constructor are JavaScript Function obtained when calling VueComponent.extend().
 * All the {@link Component} and {@link JsComponent} get a generated {@link VueComponentFactory} that wraps a
 * {@link VueJsConstructor}.
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Function")
public interface VueJsConstructor<T extends IsVueComponent>
{
    <K extends T> VueJsConstructor<K> extend(VueComponentOptions<K> vueComponentOptions);

    @JsOverlay
    default T instantiate() {
        return ((JsConstructorFn<T>) this).construct();
    }

    @JsOverlay
    default <K extends T> VueJsConstructor<K> extendJavaComponent(
        VueComponentOptions<K> componentOptions)
    {
        componentOptions.addAllProviders(getOptions().getProviders());
        VueJsConstructor<K> extendedVueJsConstructor = extend(componentOptions);
        VueGWTTools.extendVueConstructorWithJavaPrototype(extendedVueJsConstructor,
            componentOptions.getComponentExportedTypePrototype());

        return extendedVueJsConstructor;
    }

    @JsOverlay
    default VueComponentOptions<T> getOptions()
    {
        return (VueComponentOptions<T>) ((JsPropertyMap) this).get("options");
    }

    @JsOverlay
    default JsPropertyMap getOptionsComponents()
    {
        VueComponentOptions<T> options = getOptions();
        JsPropertyMap<VueComponentOptions> components = options.getComponents();
        if (components == null)
        {
            components = (JsPropertyMap<VueComponentOptions>) new JsObject();
            options.setComponents(components);
        }
        return components;
    }

    @JsOverlay
    default JsPropertyMap<VueDirectiveOptions> getOptionsDirectives()
    {
        VueComponentOptions<T> options = getOptions();
        JsPropertyMap<VueDirectiveOptions> directives = options.getDirectives();
        if (directives == null)
        {
            directives = (JsPropertyMap<VueDirectiveOptions>) new JsObject();
            options.setDirectives(directives);
        }
        return directives;
    }
}
