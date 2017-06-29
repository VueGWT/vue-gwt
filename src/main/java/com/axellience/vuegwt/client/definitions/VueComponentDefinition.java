package com.axellience.vuegwt.client.definitions;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.definitions.component.ComputedDefinition;
import com.axellience.vuegwt.client.definitions.component.ComputedKind;
import com.axellience.vuegwt.client.definitions.component.DataDefinition;
import com.axellience.vuegwt.client.definitions.component.DataFactory;
import com.axellience.vuegwt.client.definitions.component.PropDefinition;
import com.axellience.vuegwt.client.gwtextension.TemplateExpressionBase;
import com.axellience.vuegwt.client.gwtextension.TemplateExpressionKind;
import com.axellience.vuegwt.client.gwtextension.TemplateResource;
import com.axellience.vuegwt.client.jsnative.JsTools;
import com.axellience.vuegwt.client.jsnative.VueGwtTools;
import com.axellience.vuegwt.client.jsnative.types.JSON;
import com.axellience.vuegwt.client.jsnative.types.JsArray;
import com.axellience.vuegwt.client.jsnative.types.JsObject;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;
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
    @JsProperty protected VueComponent vuegwt$javaComponentInstance;

    @JsProperty protected Object el;
    @JsProperty protected String template;

    @JsProperty protected Object data;
    @JsProperty protected final JsObject computed = new JsObject();
    @JsProperty protected final JsObject methods = new JsObject();
    @JsProperty protected final JsObject watch = new JsObject();
    @JsProperty protected final JsObject props = new JsObject();
    @JsProperty protected final JsArray<String> vuegwt$collections = new JsArray<>();
    @JsProperty protected VueComponentStyle vuegwt$styles;

    @JsProperty protected final JsObject components = new JsObject();
    @JsProperty protected final JsObject directives = new JsObject();

    @JsMethod
    public void setEl(Object el)
    {
        this.el = el;
    }

    /**
     * Set the template resource for the component
     * Put back the JS function in the template expressions
     * @param templateResource
     */
    protected void setTemplateResource(TemplateResource templateResource)
    {
        this.vuegwt$styles = templateResource.getComponentStyle();

        String templateText = templateResource.getText();
        JsTools.log(templateText);
        // Empty template, nothing to do
        if ("".equals(templateText))
        {
            JsTools.unsetObjectProperty(this, "template");
            return;
        }

        // For each expression defined on our template
        for (TemplateExpressionBase expression : templateResource.getTemplateExpressions())
        {
            String expressionId = expression.getId();
            if (expression.getKind() == TemplateExpressionKind.COLLECTION)
            {
                vuegwt$collections.push(expressionId);
                methods.set(expressionId, JsTools.get(templateResource, expressionId));
            }
            else if (expression.getKind() == TemplateExpressionKind.COMPUTED_PROPERTY)
            {
                ComputedDefinition computedDefinition = new ComputedDefinition();
                computed.set(expressionId, computedDefinition);
                computedDefinition.get = JsTools.get(templateResource, expressionId);
                JsTools.log(expression.getId() + " -> " + JsTools
                    .get(templateResource, expressionId)
                    .toString());
            }
            else
            {
                methods.set(expressionId, JsTools.get(templateResource, expressionId));
                JsTools.log(expression.getId() + " -> " + JsTools
                    .get(templateResource, expressionId)
                    .toString());
            }
        }
        this.setTemplate(templateText);
    }

    protected void setTemplate(String template)
    {
        if ("".equals(template))
            JsTools.unsetObjectProperty(this, "template");
        else
            this.template = template;
    }

    protected void initData(List<DataDefinition> dataDefinitions, boolean useFactory)
    {
        JsObject dataObject = new JsObject();
        for (DataDefinition dataDefinition : dataDefinitions)
        {
            Object dataDefaultValue =
                JsTools.getObjectProperty(vuegwt$javaComponentInstance, dataDefinition.javaName);

            if (dataDefaultValue == null)
            {
                dataDefaultValue = new JsObject();
            }

            dataObject.set(dataDefinition.jsName, dataDefaultValue);
        }

        // Create JsArrays for the Java collections to be copied into
        for (String collectionId : vuegwt$collections.iterate())
        {
            dataObject.set(collectionId + TemplateResource.COLLECTION_ARRAY_SUFFIX,
                new JsArray<>());
        }

        if (useFactory)
            this.data = (DataFactory) () -> JSON.parse(JSON.stringify(dataObject));
        else
            this.data = dataObject;
    }

    protected void addMethod(String javaName)
    {
        addMethod(javaName, javaName);
    }

    protected void addMethod(String javaName, String jsName)
    {
        abstractCopyJavaMethod(methods, javaName, javaName);
    }

    protected void addComputed(String javaName, String jsName, ComputedKind kind)
    {
        ComputedDefinition computedDefinition = computed.get(jsName);
        if (computedDefinition == null)
        {
            computedDefinition = new ComputedDefinition();
            computed.set(jsName, computedDefinition);
        }

        Object method = JsTools.getObjectProperty(vuegwt$javaComponentInstance, javaName);
        if (kind == ComputedKind.GETTER)
            computedDefinition.get = method;
        else if (kind == ComputedKind.SETTER)
            computedDefinition.set = method;
    }

    protected void addWatch(String javaName, String watchedPropertyName, boolean isDeep)
    {
        if (!isDeep)
        {
            abstractCopyJavaMethod(watch, javaName, watchedPropertyName);
            return;
        }

        JsObject watchDefinition = new JsObject();
        watchDefinition.set("deep", true);
        watchDefinition.set("handler",
            JsTools.getObjectProperty(vuegwt$javaComponentInstance, javaName));
        watch.set(watchedPropertyName, watchDefinition);
    }

    protected void addLifecycleHook(String hookName)
    {
        JsTools.setObjectProperty(this,
            hookName,
            JsTools.getObjectProperty(vuegwt$javaComponentInstance, hookName));
    }

    protected void addProp(String javaName, String jsName, boolean required, String typeJsName)
    {
        PropDefinition propDefinition = new PropDefinition();
        propDefinition.required = required;
        if (JsTools.objectHasProperty(vuegwt$javaComponentInstance, javaName))
            propDefinition.defaultValue =
                JsTools.getObjectProperty(vuegwt$javaComponentInstance, javaName);

        if (typeJsName != null)
            propDefinition.type = JsTools.getNativeType(typeJsName);

        props.set(jsName, propDefinition);
    }

    protected void addPropValidator(String methodName, String propertyName)
    {
        PropDefinition propDefinition = props.get(propertyName);
        propDefinition.validator =
            JsTools.getObjectProperty(vuegwt$javaComponentInstance, methodName);
    }

    protected void addComponent(Class<? extends VueComponent> componentClass)
    {
        this.components.set(VueGwtTools.componentToTagName(componentClass),
            VueComponentDefinitionCache.getComponentDefinitionForClass(componentClass));
    }

    private void abstractCopyJavaMethod(JsObject container, String javaName, String jsName)
    {
        container.set(jsName, JsTools.getObjectProperty(vuegwt$javaComponentInstance, javaName));
    }
}
