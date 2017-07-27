package com.axellience.vuegwt.client.component.options;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.VueComponentPrototype;
import com.axellience.vuegwt.client.component.jstype.VueComponentJsTypeConstructor;
import com.axellience.vuegwt.client.component.options.computed.ComputedKind;
import com.axellience.vuegwt.client.component.options.computed.ComputedOptions;
import com.axellience.vuegwt.client.component.options.data.DataFactory;
import com.axellience.vuegwt.client.component.options.props.PropOptions;
import com.axellience.vuegwt.client.directive.options.VueDirectiveOptions;
import com.axellience.vuegwt.client.jsnative.jstypes.JSON;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.template.TemplateExpressionBase;
import com.axellience.vuegwt.client.template.TemplateExpressionKind;
import com.axellience.vuegwt.client.template.TemplateResource;
import com.axellience.vuegwt.client.tools.JsTools;
import com.google.gwt.resources.client.CssResource;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Java representation of VueComponentOptions
 * Class extending this one are generated using the Annotation processor for each VueComponent
 * <p>
 * An instance of this Class can be immediately passed to Vue.js instance where it's expecting a
 * component options object.
 * <p>
 * This is an internal Class, it shouldn't be extended in applications that use Vue GWT.
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public abstract class VueComponentOptions<T extends VueComponent> extends JsObject
{
    @JsProperty private VueComponentJsTypeConstructor<T> vuegwt$vueComponentJsTypeConstructor;
    @JsProperty private VueComponentPrototype<T> vuegwt$javaComponentProto;

    @JsProperty private Object data;
    @JsProperty private JsObject props;
    @JsProperty private JsObject propsData;

    @JsProperty private JsObject<ComputedOptions> computed;
    @JsProperty private JsObject methods;
    @JsProperty private JsObject watch;

    @JsProperty private Object el;
    @JsProperty private String template;

    @JsProperty private JsObject<VueDirectiveOptions> directives;
    @JsProperty private JsObject<VueComponentOptions> components;

    @JsProperty private VueComponent parent;
    @JsProperty private String name;

    private Map<String, CssResource> componentStyles;

    /**
     * Set the Java Component Instance on this Options
     * This instance will be used to retrieve the methods from our Options
     * @param vueComponentJsTypeConstructor An instance of the VueComponent class for this Component
     */
    @JsOverlay
    protected final void setVueComponentJsTypeConstructor(
        VueComponentJsTypeConstructor<T> vueComponentJsTypeConstructor)
    {
        this.vuegwt$vueComponentJsTypeConstructor = vueComponentJsTypeConstructor;
        this.vuegwt$javaComponentProto = vueComponentJsTypeConstructor.getComponentPrototype();

        Object customizeOption = getJavaComponentMethod("customizeOptions");
        if (customizeOption != null)
            JsTools.call(customizeOption, this, this);
    }

    /**
     * Set the template resource for the component
     * Add all the expression from the template to this Options
     * @param templateResource A generated TemplateResource
     */
    @JsOverlay
    protected final void setTemplateResource(TemplateResource templateResource)
    {
        this.initStyles(templateResource);
        this.initExpressions(templateResource);
        this.setTemplateText(templateResource.getText());
    }

    /**
     * Find styles in the template resources and ensure they are injected
     * @param templateResource A generated TemplateResource
     */
    @JsOverlay
    private void initStyles(TemplateResource templateResource)
    {
        componentStyles = new HashMap<>();
        for (Entry<String, CssResource> styleInfo : templateResource.getTemplateStyles().entrySet())
        {
            CssResource style = styleInfo.getValue();
            style.ensureInjected();
            componentStyles.put(styleInfo.getKey(), style);
        }
    }

    /**
     * Add template expressions to the ComponentOptions
     * @param templateResource A generated TemplateResource
     */
    @JsOverlay
    private void initExpressions(TemplateResource templateResource)
    {
        for (TemplateExpressionBase expression : templateResource.getTemplateExpressions())
        {
            String expressionId = expression.getId();
            if (expression.getKind() == TemplateExpressionKind.COMPUTED_PROPERTY)
            {
                ComputedOptions computedDefinition = new ComputedOptions();
                addComputedOptions(expressionId, computedDefinition);
                computedDefinition.get = JsTools.get(templateResource, expressionId);
            }
            else
            {
                addMethod(expressionId, JsTools.get(templateResource, expressionId));
            }
        }
    }

    /**
     * Set the template text
     * @param templateText The HTML string of the template (processed by the TemplateParser)
     */
    @JsOverlay
    private void setTemplateText(String templateText)
    {
        if ("".equals(templateText))
            JsTools.unsetObjectProperty(this, "template");
        else
            this.template = templateText;
    }

    /**
     * Initialise the data structure, then set it to either a Factory or directly on the Component
     * @param propertiesName List of all the name of the data properties
     * @param useFactory Boolean representing whether or not to use a Factory
     */
    @JsOverlay
    protected final void initData(List<String> propertiesName, boolean useFactory)
    {
        JsObject dataObject = new JsObject();
        for (String propertyName : propertiesName)
        {
            dataObject.set(propertyName, null);
        }

        if (useFactory)
        {
            this.setData((DataFactory) () -> {
                JsObject data = JSON.parse(JSON.stringify(dataObject));
                copyStyles(data);
                return data;
            });
        }
        else
        {
            copyStyles(dataObject);
            this.setData((DataFactory) () -> dataObject);
        }
    }

    /**
     * Copy the Component styles from GWT to the data of the ComponentOptions
     * @param data The data of the ComponentOptions
     */
    @JsOverlay
    private void copyStyles(JsObject data)
    {
        if (componentStyles == null)
            return;

        for (Entry<String, CssResource> style : componentStyles.entrySet())
            data.set(style.getKey(), style.getValue());
    }

    /**
     * Add a computed property to this ComponentOptions
     * If the computed has both a getter and a setter, this will be called twice, once for each.
     * @param javaName Name of the method in the Java Component
     * @param jsName Name of the computed property in the Template and the ComponentOptions
     * @param kind Kind of the computed method (getter or setter)
     */
    @JsOverlay
    protected final void addJavaComputed(String javaName, String jsName, ComputedKind kind)
    {
        ComputedOptions computedDefinition = getComputedOptions(jsName);
        if (computedDefinition == null)
        {
            computedDefinition = new ComputedOptions();
            addComputedOptions(jsName, computedDefinition);
        }

        Object method = getJavaComponentMethod(javaName);
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
    @JsOverlay
    protected final void addJavaWatch(String javaName, String watchedPropertyName, boolean isDeep)
    {
        if (!isDeep)
        {
            addWatch(watchedPropertyName, getJavaComponentMethod(javaName));
            return;
        }

        JsObject watchDefinition = new JsObject();
        watchDefinition.set("deep", true);
        watchDefinition.set("handler", getJavaComponentMethod(javaName));
        addWatch(watchedPropertyName, watchDefinition);
    }

    /**
     * Add the given lifecycle hook to the ComponentOptions
     * @param hookName Name of the hook to add
     */
    @JsOverlay
    protected final void addJavaLifecycleHook(String hookName)
    {
        set(hookName, getJavaComponentMethod(hookName));
    }

    /**
     * Add a prop to our ComponentOptions
     * This will allow to receive data from the outside of our Component
     * @param javaName The name of the property in our Java Component
     * @param jsName The name of the property in the Template and the ComponentOptions
     * @param required Is the property required (mandatory)
     * @param typeJsName JS name of the type of this property, if not null we will ask Vue to type
     * check based on it
     */
    @JsOverlay
    protected final void addJavaProp(String javaName, String jsName, boolean required,
        String typeJsName)
    {
        PropOptions propDefinition = new PropOptions();
        propDefinition.required = required;
        if (vuegwt$javaComponentProto.hasOwnProperty(javaName))
            propDefinition.defaultValue = getJavaComponentMethod(javaName);

        if (typeJsName != null)
            propDefinition.type = JsTools.getWindow().get(typeJsName);

        addProp(jsName, propDefinition);
    }

    /**
     * Add a custom prop validator to validate a property
     * @param methodName The name of the method in the Java Component
     * @param propertyName The name of the property to validate
     */
    @JsOverlay
    protected final void addJavaPropValidator(String methodName, String propertyName)
    {
        PropOptions propDefinition = (PropOptions) props.get(propertyName);
        propDefinition.validator = getJavaComponentMethod(methodName);
    }

    @JsOverlay
    private Object getJavaComponentMethod(String javaName)
    {
        return vuegwt$javaComponentProto.get(javaName);
    }

    @JsOverlay
    public final Object getData()
    {
        return data;
    }

    @JsOverlay
    public final VueComponentOptions setData(Object data)
    {
        this.data = data;
        return this;
    }

    @JsOverlay
    public final JsObject getProps()
    {
        return props;
    }

    @JsOverlay
    public final VueComponentOptions setProps(JsObject props)
    {
        this.props = props;
        return this;
    }

    @JsOverlay
    public final VueComponentOptions addProp(String name, PropOptions propOptions)
    {
        if (this.props == null)
            this.props = new JsObject();

        this.props.set(name, propOptions);
        return this;
    }

    @JsOverlay
    public final JsObject getPropsData()
    {
        return propsData;
    }

    @JsOverlay
    public final VueComponentOptions setPropsData(JsObject propsData)
    {
        this.propsData = propsData;
        return this;
    }

    @JsOverlay
    public final JsObject<ComputedOptions> getComputed()
    {
        return computed;
    }

    @JsOverlay
    public final VueComponentOptions setComputed(JsObject<ComputedOptions> computed)
    {
        this.computed = computed;
        return this;
    }

    @JsOverlay
    public final VueComponentOptions addComputedOptions(String name, ComputedOptions computed)
    {
        if (this.computed == null)
            this.computed = new JsObject<>();

        this.computed.set(name, computed);
        return this;
    }

    @JsOverlay
    public final ComputedOptions getComputedOptions(String name)
    {
        if (this.computed == null)
            this.computed = new JsObject<>();

        return this.computed.get(name);
    }

    @JsOverlay
    public final JsObject getMethods()
    {
        return methods;
    }

    @JsOverlay
    public final VueComponentOptions setMethods(JsObject methods)
    {
        this.methods = methods;
        return this;
    }

    @JsOverlay
    public final VueComponentOptions addMethod(String name, Object method)
    {
        if (this.methods == null)
            this.methods = new JsObject<>();

        this.methods.set(name, method);
        return this;
    }

    @JsOverlay
    public final JsObject getWatch()
    {
        return watch;
    }

    @JsOverlay
    public final VueComponentOptions setWatch(JsObject watch)
    {
        this.watch = watch;
        return this;
    }

    @JsOverlay
    public final VueComponentOptions addWatch(String name, Object watcher)
    {
        if (this.watch == null)
            this.watch = new JsObject<>();

        this.watch.set(name, watcher);
        return this;
    }

    @JsOverlay
    public final Object getEl()
    {
        return el;
    }

    @JsOverlay
    public final void setEl(Object el)
    {
        this.el = el;
    }

    @JsOverlay
    public final String getTemplate()
    {
        return template;
    }

    @JsOverlay
    public final VueComponentOptions setTemplate(String template)
    {
        this.template = template;
        return this;
    }

    @JsOverlay
    public final JsObject<VueDirectiveOptions> getDirectives()
    {
        return directives;
    }

    @JsOverlay
    public final VueComponentOptions setDirectives(JsObject<VueDirectiveOptions> directives)
    {
        this.directives = directives;
        return this;
    }

    @JsOverlay
    public final JsObject<VueComponentOptions> getComponents()
    {
        return components;
    }

    @JsOverlay
    public final VueComponentOptions setComponents(JsObject<VueComponentOptions> components)
    {
        this.components = components;
        return this;
    }

    @JsOverlay
    public final VueComponent getParent()
    {
        return parent;
    }

    @JsOverlay
    public final VueComponentOptions setParent(VueComponent parent)
    {
        this.parent = parent;
        return this;
    }

    @JsOverlay
    public final String getName()
    {
        return name;
    }

    @JsOverlay
    public final VueComponentOptions setName(String name)
    {
        this.name = name;
        return this;
    }

    @JsOverlay
    public final Map<String, CssResource> getComponentStyles()
    {
        return componentStyles;
    }
}
