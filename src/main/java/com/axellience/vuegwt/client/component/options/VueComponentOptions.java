package com.axellience.vuegwt.client.component.options;

import com.axellience.vuegwt.client.component.ComponentJavaPrototype;
import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.options.computed.ComputedKind;
import com.axellience.vuegwt.client.component.options.computed.ComputedOptions;
import com.axellience.vuegwt.client.component.options.data.DataFactory;
import com.axellience.vuegwt.client.component.options.props.PropOptions;
import com.axellience.vuegwt.client.component.template.TemplateResource;
import com.axellience.vuegwt.client.directive.options.VueDirectiveOptions;
import com.axellience.vuegwt.client.jsnative.jstypes.JSON;
import com.axellience.vuegwt.client.jsnative.jstypes.JsArray;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.tools.JsTools;
import com.google.gwt.resources.client.CssResource;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static com.axellience.vuegwt.client.tools.JsTools.isUndefined;

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
public class VueComponentOptions<T extends VueComponent> extends JsObject
{
    private ComponentJavaPrototype<T> componentJavaPrototype;
    private TemplateResource<T> templateResource;
    private Map<String, Provider<?>> dependenciesProvider;
    private JsObject dataFields;

    /**
     * Set the Java Prototype on this {@link VueComponentOptions}.
     * This prototype will be used to retrieve the java methods of our {@link VueComponent}.
     * @param javaPrototype An instance of the {@link TemplateResource} class for this
     * Component
     */
    @JsOverlay
    public final void setComponentJavaPrototype(ComponentJavaPrototype<T> javaPrototype)
    {
        this.componentJavaPrototype = javaPrototype;
    }

    /**
     * Set the {@link TemplateResource} on this {@link VueComponentOptions}.
     * This instance will be used to retrieve the java methods of our {@link VueComponent}.
     * @param templateResource An instance of the {@link TemplateResource} class for this
     * Component
     */
    @JsOverlay
    public final void setTemplateResource(TemplateResource<T> templateResource)
    {
        this.templateResource = templateResource;
        this.injectStyles();
        this.initExpressions();
        this.initRenderFunctions();
    }

    /**
     * Find styles in the {@link TemplateResource} and ensure they are injected.
     */
    @JsOverlay
    private void injectStyles()
    {
        for (CssResource style : templateResource.getTemplateStyles().values())
        {
            style.ensureInjected();
        }
    }

    /**
     * Add template expressions to this {@link VueComponentOptions}.
     */
    @JsOverlay
    private void initExpressions()
    {
        for (String methodId : templateResource.getTemplateMethods())
        {
            addMethod(methodId, JsTools.get(templateResource, methodId));
        }
    }

    /**
     * Initialise the render functions from our template.
     */
    @JsOverlay
    private void initRenderFunctions()
    {
        this.set("render", JsTools.createFunction(templateResource.getRenderFunction()));

        JsArray<Object> staticRenderFns = new JsArray<>();
        for (String staticRenderFunction : templateResource.getStaticRenderFunctions())
        {
            staticRenderFns.push(JsTools.createFunction(staticRenderFunction));
        }
        this.setStaticRenderFns(staticRenderFns);
    }

    /**
     * Initialise the data structure, then set it to either a Factory or directly on the Component.
     * @param useFactory Boolean representing whether or not to use a Factory.
     */
    @JsOverlay
    public final void initData(boolean useFactory, String... fieldNames)
    {
        dataFields = new JsObject();
        for (String fieldName : fieldNames)
        {
            // Get the default field value from the prototype if any
            Object defaultValue = componentJavaPrototype.get(fieldName);
            if (!isUndefined(defaultValue))
                dataFields.set(fieldName, defaultValue);
            else
                dataFields.set(fieldName, null);
        }

        if (useFactory)
        {
            String dataFieldsJSON = JSON.stringify(dataFields);
            this.setData((DataFactory) () -> {
                JsObject data = JSON.parse(dataFieldsJSON);
                addStylesToData(data);
                return data;
            });
        }
        else
        {
            addStylesToData(dataFields);
            this.setData((DataFactory) () -> dataFields);
        }
    }

    /**
     * Copy the Component styles from GWT to the data of the ComponentOptions.
     * @param data The data of the ComponentOptions
     */
    @JsOverlay
    private void addStylesToData(JsObject data)
    {
        if (templateResource == null || templateResource.getTemplateStyles() == null)
            return;

        for (Entry<String, CssResource> style : templateResource.getTemplateStyles().entrySet())
            data.set(style.getKey(), style.getValue());
    }

    /**
     * Add a computed property to this ComponentOptions.
     * If the computed has both a getter and a setter, this will be called twice, once for each.
     * @param javaMethodName Name of the method in the {@link TemplateResource}
     * @param computedPropertyName Name of the computed property in the Template and the
     * ComponentOptions
     * @param kind Kind of the computed method (getter or setter)
     */
    @JsOverlay
    public final void addJavaComputed(String javaMethodName, String computedPropertyName,
        ComputedKind kind)
    {
        ComputedOptions computedDefinition = getComputedOptions(computedPropertyName);
        if (computedDefinition == null)
        {
            computedDefinition = new ComputedOptions();
            addComputedOptions(computedPropertyName, computedDefinition);
        }

        Object method = getJavaComponentMethod(javaMethodName);
        if (kind == ComputedKind.GETTER)
            computedDefinition.get = method;
        else if (kind == ComputedKind.SETTER)
            computedDefinition.set = method;
    }

    /**
     * Add a watch property to this Component Definition
     * @param javaMethodName Name of the method in the {@link TemplateResource}
     * @param watchedPropertyName Name of the property name to watch in the data model
     * @param isDeep Is the watcher deep (will watch child properties)
     */
    @JsOverlay
    public final void addJavaWatch(String javaMethodName, String watchedPropertyName,
        boolean isDeep)
    {
        if (!isDeep)
        {
            addWatch(watchedPropertyName, getJavaComponentMethod(javaMethodName));
            return;
        }

        JsObject watchDefinition = new JsObject();
        watchDefinition.set("deep", true);
        watchDefinition.set("handler", getJavaComponentMethod(javaMethodName));
        addWatch(watchedPropertyName, watchDefinition);
    }

    @JsOverlay
    public final void addMethods(String... javaMethodNames)
    {
        for (String javaMethodName : javaMethodNames)
        {
            this.addMethod(javaMethodName, getJavaComponentMethod(javaMethodName));
        }
    }

    /**
     * Add the given lifecycle hook to the {@link VueComponentOptions}.
     * @param hookName Name of the hook to add
     */
    @JsOverlay
    public final void addHookMethod(String hookName)
    {
        addHookMethod(hookName, hookName);
    }

    /**
     * Add the given lifecycle hook to the {@link VueComponentOptions}.
     * @param hookName Name of the hook to add
     * @param javaMethodName Name of the java method for the hook
     */
    @JsOverlay
    public final void addHookMethod(String hookName, String javaMethodName)
    {
        set(hookName, getJavaComponentMethod(javaMethodName));
    }

    /**
     * Add a prop to our ComponentOptions.
     * This will allow to receive data from the outside of our Component.
     * @param propName The name of the property
     * @param required Is the property required (mandatory)
     * @param jsTypeName JS name of the type of this property, if not null we will ask Vue to type
     * check based on it
     */
    @JsOverlay
    public final void addJavaProp(String propName, boolean required, String jsTypeName)
    {
        PropOptions propDefinition = new PropOptions();
        propDefinition.required = required;

        if (jsTypeName != null)
            propDefinition.type = JsTools.getWindow().get(jsTypeName);

        addProp(propName, propDefinition);
    }

    /**
     * Add a custom prop validator to validate a property
     * @param javaMethodName Name of the method in the {@link TemplateResource}
     * @param propertyName The name of the property to validate
     */
    @JsOverlay
    public final void addJavaPropValidator(String javaMethodName, String propertyName)
    {
        PropOptions propDefinition = (PropOptions) props.get(propertyName);
        propDefinition.validator = getJavaComponentMethod(javaMethodName);
    }

    /**
     * Add a custom prop validator to validate a property
     * @param javaMethodName Name of the method in the {@link TemplateResource}
     * @param propertyName The name of the property to validate
     */
    @JsOverlay
    public final void addJavaPropDefaultValue(String javaMethodName, String propertyName)
    {
        PropOptions propDefinition = (PropOptions) props.get(propertyName);
        propDefinition.defaultValue = getJavaComponentMethod(javaMethodName);
    }

    /**
     * Get the given java method from the {@link TemplateResource}.
     * @param javaMethodName Name of the Java method to retrieve
     * @return The JS function that represent our Java method.
     */
    @JsOverlay
    private Object getJavaComponentMethod(String javaMethodName)
    {
        return componentJavaPrototype.get(javaMethodName);
    }

    /**
     * Return the prototype for our Component Java object. We can use it to get methods from.
     * @return The prototype of our Component Java object
     */
    @JsOverlay
    public final ComponentJavaPrototype<T> getComponentJavaPrototype()
    {
        return componentJavaPrototype;
    }

    @JsOverlay
    public final Provider<?> getProvider(Class<T> component)
    {
        return getProviders().get(component.getCanonicalName());
    }

    @JsOverlay
    public final void addProvider(Class<T> component, Provider<?> dependenciesProvider)
    {
        getProviders().put(component.getCanonicalName(), dependenciesProvider);
    }

    @JsOverlay
    public final void addAllProviders(Map<String, Provider<?>> providers)
    {
        getProviders().putAll(providers);
    }

    @JsOverlay
    public final Map<String, Provider<?>> getProviders()
    {
        if (this.dependenciesProvider == null)
            this.dependenciesProvider = new HashMap<>();

        return dependenciesProvider;
    }

    /* ---------------------------------------------

              Instance Properties and Methods

      ---------------------------------------------*/
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

    @JsProperty private JsArray<Object> staticRenderFns;

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
    public final JsArray<Object> getStaticRenderFns()
    {
        return staticRenderFns;
    }

    @JsOverlay
    public final VueComponentOptions setStaticRenderFns(JsArray<Object> staticRenderFns)
    {
        this.staticRenderFns = staticRenderFns;
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
}
