package com.axellience.vuegwt.client.jsnative.definitions;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.jsnative.JsArray;
import jsinterop.annotations.JsType;

@JsType
public abstract class ComponentDefinition
{
    protected VueComponent javaComponentInstance;

    protected JsArray<String> dataPropertiesNames;
    protected JsArray<String> methodsNames;

    protected JsArray<NamedVueProperty> computed = new JsArray<>();
    protected JsArray<NamedVueProperty> watched = new JsArray<>();

    public VueComponent getJavaComponentInstance()
    {
        return javaComponentInstance;
    }

    public JsArray<String> getDataPropertiesNames()
    {
        return dataPropertiesNames;
    }

    public JsArray<String> getMethodsNames()
    {
        return methodsNames;
    }

    public JsArray<NamedVueProperty> getComputed()
    {
        return computed;
    }

    public JsArray<NamedVueProperty> getWatched()
    {
        return watched;
    }
}
