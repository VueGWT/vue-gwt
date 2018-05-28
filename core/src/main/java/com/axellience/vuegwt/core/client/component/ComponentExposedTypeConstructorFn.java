package com.axellience.vuegwt.core.client.component;

import com.axellience.vuegwt.core.client.vue.VueComponentFactory;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsConstructorFn;
import jsinterop.base.JsPropertyMap;

/**
 * Constructor function for the ComponentExposedType.
 * They are exposed in VueGWTComponents on window
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Function")
public interface ComponentExposedTypeConstructorFn<T extends IsVueComponent>
{
    /**
     * Return the ExportedType Java class JS prototype.
     * @return The JS prototype of the ComponentExposedType class
     */
    @JsProperty
    JsPropertyMap<Object> getPrototype();

    VueComponentFactory<T> getVueComponentFactory();

    /**
     * Init instance properties for the given VueComponent instance.
     * The Constructor for VueComponent is provided by Vue and doesn't extend the {@link IsVueComponent}
     * constructor.
     * This method will create an instance of the ExposedType and copy properties to the VueComponentInstance.
     * This will initialise properties that are initialised inline in the class.
     * For example: List&lt;String&gt; myList = new LinkedList&lt;String&gt;();
     * @param componentInstance An instance of VueComponent to initialize
     */
    @JsOverlay
    default void initComponentInstanceProperties(T componentInstance)
    {
        JsPropertyMap<Object> componentInstancePropertyMap = Js.cast(componentInstance);
        JsPropertyMap<Object> exposedTypeInstance =
            Js.cast(((JsConstructorFn<T>) this).construct());

        exposedTypeInstance.forEach(key -> {
            if (!exposedTypeInstance.has(key) || componentInstancePropertyMap.get(key) != null)
                return;
            componentInstancePropertyMap.set(key, exposedTypeInstance.get(key));
        });
    }
}
