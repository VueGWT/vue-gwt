package com.axellience.vuegwt.client.definitions;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.definitions.component.DataDefinition;
import com.axellience.vuegwt.client.definitions.component.DataFactory;
import com.axellience.vuegwt.client.jsnative.JSON;
import com.axellience.vuegwt.client.jsnative.JsArray;
import com.axellience.vuegwt.client.jsnative.JsObject;
import com.axellience.vuegwt.client.jsnative.JsTools;
import com.axellience.vuegwt.client.jsnative.VueGwtTools;
import jsinterop.annotations.JsType;

import java.util.List;

/**
 * Java representation of a Vue Component definition
 * Class extending this one are generated using the Annotation processor for each VueComponent
 * <p>
 * An instance of this Class can be immediately passed to Vue.JS instance where it's expecting a
 * component definition object.
 * <p>
 * This is an internal Class, it shouldn't be manipulated in applications that use VueGWT.
 * @author Adrien Baron
 */
@JsType
public abstract class VueComponentDefinition
{
    public VueComponent vuegwt$javaComponentInstance;

    public Object el;
    public String template;

    public Object data;
    public final JsObject computed     = new JsObject();
    public final JsObject methods      = new JsObject();
    public final JsObject watched      = new JsObject();
    public final JsArray<String> props = new JsArray<>();

    public final JsObject components = new JsObject();
    public final JsObject directives = new JsObject();

    protected void initData(List<DataDefinition> dataDefinitions, boolean useFactory)
    {
        JsObject dataObject = new JsObject();
        for (DataDefinition dataDefinition : dataDefinitions)
        {
            dataObject.set(dataDefinition.jsName,
                JsTools.getObjectProperty(vuegwt$javaComponentInstance, dataDefinition.javaName)
            );
        }

        if (useFactory)
            this.data = (DataFactory) () -> JSON.parse(JSON.stringify(dataObject));
        else
            this.data = dataObject;
    }

    public void setEl(Object el)
    {
        this.el = el;
    }

    protected void setTemplate(String template)
    {
        if ("".equals(template))
            JsTools.unsetObjectProperty(this, "template");
        else
            this.template = template;
    }

    protected void addMethod(String javaName)
    {
        addMethod(javaName, javaName);
    }

    protected void addMethod(String javaName, String jsName)
    {
        abstractCopyJavaMethod(methods, javaName, javaName);
    }

    protected void addComputed(String javaName, String jsName)
    {
        abstractCopyJavaMethod(computed, javaName, jsName);
    }

    protected void addWatch(String javaName, String jsName)
    {
        abstractCopyJavaMethod(watched, javaName, jsName);
    }

    protected void addLifecycleHook(String hookName)
    {
        JsTools.setObjectProperty(
            this, hookName, JsTools.getObjectProperty(vuegwt$javaComponentInstance, hookName));
    }

    protected void addProp(String jsName)
    {
        props.push(jsName);
    }

    protected void addComponent(Class<? extends VueComponent> componentClass)
    {
        this.components.set(VueGwtTools.componentToTagName(componentClass),
            VueComponentDefinitionCache.getComponentDefinitionForClass(componentClass)
        );
    }

    private void abstractCopyJavaMethod(JsObject container, String javaName, String jsName)
    {
        container.set(jsName, JsTools.getObjectProperty(vuegwt$javaComponentInstance, javaName));
    }
}
