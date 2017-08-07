package com.axellience.vuegwt.client.component.options;

import com.axellience.vuegwt.client.component.HasCustomizeOptions;
import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.options.computed.ComputedKind;
import com.axellience.vuegwt.client.component.options.computed.ComputedOptions;
import com.axellience.vuegwt.client.component.options.data.DataFactory;
import com.axellience.vuegwt.client.component.options.props.PropOptions;
import com.axellience.vuegwt.client.component.template.ComponentWithTemplate;
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
    private ComponentWithTemplate<T> componentWithTemplate;
    private Map<String, Provider<?>> componentWithTemplateProviders;
    private JsObject dataFields;

    /**
     * Set the {@link ComponentWithTemplate} on this {@link VueComponentOptions}.
     * This instance will be used to retrieve the java methods of our {@link VueComponent}.
     * @param componentWithTemplate An instance of the {@link ComponentWithTemplate} class for this
     * Component
     */
    @JsOverlay
    public final void setComponentWithTemplate(ComponentWithTemplate<T> componentWithTemplate)
    {
        this.componentWithTemplate = componentWithTemplate;

        if (componentWithTemplate.getRenderFunction() != null)
        {
            this.injectStyles();
            this.initExpressions();
            this.initRenderFunctions();
        }
    }

    /**
     * Find styles in the {@link ComponentWithTemplate} and ensure they are injected.
     */
    @JsOverlay
    private void injectStyles()
    {
        for (CssResource style : componentWithTemplate.getTemplateStyles().values())
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
        for (String expressionId : componentWithTemplate.getTemplateComputedProperties())
        {
            addJavaComputed(expressionId, expressionId, ComputedKind.GETTER);
        }
    }

    /**
     * Initialise the render functions from our template.
     */
    @JsOverlay
    private void initRenderFunctions()
    {
        this.set("render", JsTools.createFunction(componentWithTemplate.getRenderFunction()));

        JsArray<Object> staticRenderFns = new JsArray<>();
        for (String staticRenderFunction : componentWithTemplate.getStaticRenderFunctions())
        {
            staticRenderFns.push(JsTools.createFunction(staticRenderFunction));
        }
        this.setStaticRenderFns(staticRenderFns);
    }

    /**
     * Add a data field to the Component. Vue.js will observe all this fields when the Component
     * will be bootstrapped.
     * @param fieldName Name of the field
     */
    @JsOverlay
    public final void addDataField(String fieldName)
    {
        if (dataFields == null)
            dataFields = new JsObject();

        dataFields.set(fieldName, null);
    }

    /**
     * Initialise the data structure, then set it to either a Factory or directly on the Component.
     * @param useFactory Boolean representing whether or not to use a Factory.
     */
    @JsOverlay
    public final void initData(boolean useFactory)
    {
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
        if (componentWithTemplate.getTemplateStyles() == null)
            return;

        for (Entry<String, CssResource> style : componentWithTemplate
            .getTemplateStyles()
            .entrySet())
            data.set(style.getKey(), style.getValue());
    }

    /**
     * Add a computed property to this ComponentOptions.
     * If the computed has both a getter and a setter, this will be called twice, once for each.
     * @param javaMethodName Name of the method in the {@link ComponentWithTemplate}
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
     * @param javaMethodName Name of the method in the {@link ComponentWithTemplate}
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

    /**
     * Add the given lifecycle hook to the {@link VueComponentOptions}.
     * @param hookName Name of the hook to add
     */
    @JsOverlay
    public final void addRootJavaMethod(String hookName)
    {
        set(hookName, getJavaComponentMethod(hookName));
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
     * @param javaMethodName Name of the method in the {@link ComponentWithTemplate}
     * @param propertyName The name of the property to validate
     */
    @JsOverlay
    public final void addJavaPropValidator(String javaMethodName, String propertyName)
    {
        PropOptions propDefinition = (PropOptions) props.get(propertyName);
        propDefinition.validator = getJavaComponentMethod(javaMethodName);
    }

    /**
     * Get the given java method from the {@link ComponentWithTemplate}.
     * @param javaMethodName Name of the Java method to retrieve
     * @return The JS function that represent our Java method.
     */
    @JsOverlay
    private Object getJavaComponentMethod(String javaMethodName)
    {
        return JsTools.get(componentWithTemplate, javaMethodName);
    }

    /**
     * Return the {@link ComponentWithTemplate} instance.
     * @return The instance of {@link ComponentWithTemplate} we use to get the methods from
     */
    @JsOverlay
    public final ComponentWithTemplate<T> getComponentWithTemplate()
    {
        return componentWithTemplate;
    }

    @JsOverlay
    public final Provider<?> getProvider(Class<T> component)
    {
        return getProviders().get(component.getCanonicalName());
    }

    @JsOverlay
    public final void addProvider(Class<T> component, Provider<?> componentWithTemplateProvider)
    {
        // Customize options with the provided ComponentWithTemplate
        // This allows for example to declare route in a component with VueRouter
        ComponentWithTemplate componentWithTemplate =
            (ComponentWithTemplate) componentWithTemplateProvider.get();

        if (componentWithTemplate instanceof HasCustomizeOptions)
            ((HasCustomizeOptions) componentWithTemplate).customizeOptions(this);

        getProviders().put(component.getCanonicalName(), componentWithTemplateProvider);
    }

    @JsOverlay
    public final void addAllProviders(Map<String, Provider<?>> providers)
    {
        getProviders().putAll(providers);
    }

    @JsOverlay
    public final Map<String, Provider<?>> getProviders()
    {
        if (this.componentWithTemplateProviders == null)
            this.componentWithTemplateProviders = new HashMap<>();

        return componentWithTemplateProviders;
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
