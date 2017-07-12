package com.axellience.vuegwt.client.definitions;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.VueDirective;
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
import com.axellience.vuegwt.client.jsnative.types.JsObject;
import com.google.gwt.resources.client.CssResource;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Java representation of a Vue Component definition
 * Class extending this one are generated using the Annotation processor for each VueComponent
 * <p>
 * An instance of this Class can be immediately passed to Vue.js instance where it's expecting a
 * component definition object.
 * <p>
 * This is an internal Class, it shouldn't be extended in applications that use VueGWT.
 * @author Adrien Baron
 */
@JsType
public abstract class VueComponentDefinition<T extends VueComponent> extends JsObject
{
    @JsProperty protected T vuegwt$javaComponentInstance;

    @JsProperty protected String name;

    @JsProperty protected Object el;
    @JsProperty protected String template;

    @JsProperty protected Object data;
    @JsProperty protected final JsObject computed = new JsObject();
    @JsProperty protected final JsObject methods = new JsObject();
    @JsProperty protected final JsObject watch = new JsObject();
    @JsProperty protected final JsObject props = new JsObject();

    @JsProperty protected final JsObject components = new JsObject();
    @JsProperty protected final JsObject directives = new JsObject();

    @JsMethod
    public void setEl(Object el)
    {
        this.el = el;
    }

    private final Map<String, CssResource> componentStyles = new HashMap<>();

    private final Set<Class<? extends VueComponent>> localComponents = new HashSet<>();
    private final Set<Class<? extends VueDirective>> localDirectives = new HashSet<>();
    private boolean areDependenciesInjected = false;

    /**
     * Set the Java Component Instance on this definition
     * This instance will be used to retrieve the methods from our Definition
     * @param javaComponentInstance An instance of the VueComponent class for this Component
     */
    protected void setJavaComponentInstance(T javaComponentInstance)
    {
        this.vuegwt$javaComponentInstance = javaComponentInstance;
        this.vuegwt$javaComponentInstance.customizeDefinition(this);
    }

    /**
     * Return the Java Component Instance for this definition
     * This instance is used to retrieve the methods from our Definition
     * @return An instance of the VueComponent class for this Component
     */
    @JsIgnore
    public T getJavaComponentInstance()
    {
        return this.vuegwt$javaComponentInstance;
    }

    /**
     * Set the template resource for the component
     * Add all the expression from the template to this definition
     * @param templateResource A generated TemplateResource
     */
    protected void setTemplateResource(TemplateResource templateResource)
    {
        this.initStyles(templateResource);
        this.initExpressions(templateResource);
        this.setTemplateText(templateResource.getText());
    }

    /**
     * Find styles in the template resources and ensure they are injected
     * @param templateResource A generated TemplateResource
     */
    private void initStyles(TemplateResource templateResource)
    {
        for (Entry<String, CssResource> styleInfo : templateResource.getTemplateStyles().entrySet())
        {
            CssResource style = styleInfo.getValue();
            style.ensureInjected();
            componentStyles.put(styleInfo.getKey(), style);
        }
    }

    /**
     * Add template expressions to the ComponentDefinition
     * @param templateResource A generated TemplateResource
     */
    private void initExpressions(TemplateResource templateResource)
    {
        for (TemplateExpressionBase expression : templateResource.getTemplateExpressions())
        {
            String expressionId = expression.getId();
            if (expression.getKind() == TemplateExpressionKind.COMPUTED_PROPERTY)
            {
                ComputedDefinition computedDefinition = new ComputedDefinition();
                computed.set(expressionId, computedDefinition);
                computedDefinition.get = JsTools.get(templateResource, expressionId);
            }
            else
            {
                methods.set(expressionId, JsTools.get(templateResource, expressionId));
            }
        }
    }

    /**
     * Set the template text
     * @param templateText The HTML string of the template (processed by the TemplateParser)
     */
    private void setTemplateText(String templateText)
    {
        if ("".equals(templateText))
            JsTools.unsetObjectProperty(this, "template");
        else
            this.template = templateText;
    }

    /**
     * Initialise the data structure, then set it to either a Factory or directly on the Component
     * @param dataDefinitions List of all the name of the data properties
     * @param useFactory Boolean representing whether or not to use a Factory
     */
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

        if (useFactory)
        {
            this.data = (DataFactory) () ->
            {
                JsObject data = JSON.parse(JSON.stringify(dataObject));
                copyStyles(data);
                return data;
            };
        }
        else
        {
            copyStyles(dataObject);
            this.data = dataObject;
        }
    }

    /**
     * Copy the Component styles from GWT to the data of the Component Definition
     * @param data The data of the Component Definition
     */
    private void copyStyles(JsObject data)
    {
        for (Entry<String, CssResource> style : componentStyles.entrySet())
        {
            data.set(style.getKey(), style.getValue());
        }
    }

    /**
     * Add a method to this Component Definition
     * @param javaName Name of the method in the Java Component
     */
    protected void addMethod(String javaName)
    {
        addMethod(javaName, javaName);
    }

    /**
     * Add a method to this Component Definition
     * @param javaName Name of the method in the Java Component
     * @param jsName Name of the method in the Template and the Component Definition
     */
    protected void addMethod(String javaName, String jsName)
    {
        abstractCopyJavaMethod(methods, javaName, jsName);
    }

    /**
     * Add a computed property to this Component Definition
     * If the computed has both a getter and a setter, this will be called twice, once for each.
     * @param javaName Name of the method in the Java Component
     * @param jsName Name of the computed property in the Template and the Component Definition
     * @param kind Kind of the computed method (getter or setter)
     */
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

    /**
     * Add a watch property to this Component Definition
     * @param javaName Name of the method in the Java Component
     * @param watchedPropertyName Name of the property name to watch in the data model
     * @param isDeep Is the watcher deep (will watch child properties)
     */
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

    /**
     * Add the given lifecycle hook to the Component Definition
     * @param hookName Name of the hook to add
     */
    protected void addLifecycleHook(String hookName)
    {
        set(hookName, JsTools.getObjectProperty(vuegwt$javaComponentInstance, hookName));
    }

    /**
     * Add a prop to our Component Definition
     * This will allow to receive data from the outside of our Component
     * @param javaName The name of the property in our Java Component
     * @param jsName The name of the property in the Template and the Component Definition
     * @param required Is the property required (mandatory)
     * @param typeJsName JS name of the type of this property, if not null we will ask Vue to type
     * check based on it
     */
    protected void addProp(String javaName, String jsName, boolean required, String typeJsName)
    {
        PropDefinition propDefinition = new PropDefinition();
        propDefinition.required = required;
        if (JsTools.objectHasProperty(vuegwt$javaComponentInstance, javaName))
            propDefinition.defaultValue =
                JsTools.getObjectProperty(vuegwt$javaComponentInstance, javaName);

        if (typeJsName != null)
            propDefinition.type = JsTools.getWindow().get(typeJsName);

        props.set(jsName, propDefinition);
    }

    /**
     * Add a custom prop validator to validate a property
     * @param methodName The name of the method in the Java Component
     * @param propertyName The name of the property to validate
     */
    protected void addPropValidator(String methodName, String propertyName)
    {
        PropDefinition propDefinition = props.get(propertyName);
        propDefinition.validator =
            JsTools.getObjectProperty(vuegwt$javaComponentInstance, methodName);
    }

    /**
     * Register a local component in the components property of our Component Definition
     * The registration will actually take place the first time our Component Definition is accessed
     * @param componentClass
     */
    protected void addLocalComponent(Class<? extends VueComponent> componentClass)
    {
        this.localComponents.add(componentClass);
    }

    /**
     * Register a local directive in the components property of our Component Definition
     * The registration will actually take place the first time our Component Definition is accessed
     * @param directiveClass
     */
    protected void addLocalDirective(Class<? extends VueDirective> directiveClass)
    {
        this.localDirectives.add(directiveClass);
    }

    /**
     * Get the definitions of local components and directives
     * This will be called each time a Component definition is retrieved
     */
    protected void ensureDependenciesInjected()
    {
        if (areDependenciesInjected)
            return;
        this.areDependenciesInjected = true;

        for (Class<? extends VueComponent> childComponentClass : localComponents)
        {
            VueComponentDefinition childComponentDefinition =
                VueDefinitionCache.getComponentDefinitionForClass(childComponentClass);
            this.components.set(VueGwtTools.componentToTagName(childComponentClass),
                childComponentDefinition);
        }

        for (Class<? extends VueDirective> childDirectiveClass : localDirectives)
        {
            VueDirectiveDefinition childDirectiveDefinition =
                VueDefinitionCache.getDirectiveDefinitionForClass(childDirectiveClass);
            this.directives.set(VueGwtTools.directiveToTagName(childDirectiveClass),
                childDirectiveDefinition);
        }
    }

    /**
     * Copy a Java method method from our Java Component Instance to the given container
     * @param container A JsObject to get our Java Method
     * @param javaName The name of Java method in our Component Class
     * @param jsName The name we want our method to have in JavaScript
     */
    private void abstractCopyJavaMethod(JsObject container, String javaName, String jsName)
    {
        container.set(jsName, JsTools.getObjectProperty(vuegwt$javaComponentInstance, javaName));
    }
}
