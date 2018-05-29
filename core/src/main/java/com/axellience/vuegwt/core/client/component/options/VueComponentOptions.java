package com.axellience.vuegwt.core.client.component.options;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.options.computed.ComputedKind;
import com.axellience.vuegwt.core.client.component.options.computed.ComputedOptions;
import com.axellience.vuegwt.core.client.component.options.data.DataFactory;
import com.axellience.vuegwt.core.client.component.options.props.PropOptions;
import com.axellience.vuegwt.core.client.directive.options.VueDirectiveOptions;
import elemental2.core.Function;
import elemental2.core.JsArray;
import elemental2.core.JsObject;
import elemental2.dom.DomGlobal;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;

import static elemental2.core.Global.JSON;

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
public class VueComponentOptions<T extends IsVueComponent> implements JsPropertyMap
{
    private JsPropertyMap<Object> componentExportedTypePrototype;
    private Map<String, Provider<?>> dependenciesProvider;

    /**
     * Set the JS Prototype of the ExportedType Java Class represented by this {@link VueComponentOptions}.
     * This prototype will be used to retrieve the java methods of our {@link IsVueComponent}.
     * @param prototype The JS prototype of the ExportedType Java Class
     */
    @JsOverlay
    public final void setComponentExportedTypePrototype(JsPropertyMap<Object> prototype)
    {
        this.componentExportedTypePrototype = prototype;
        // This must be set for Components extending Native JS Components
        this.componentExportedTypePrototype.set("options", this);
        this.initTemplateExpressions();
    }

    /**
     * Add template expressions to this {@link VueComponentOptions}.
     */
    @JsOverlay
    private void initTemplateExpressions()
    {
        int i = 0;
        Function templateMethod;
        while ((templateMethod = getJavaComponentMethod("exp$" + i)) != null)
        {
            addMethod("exp$" + i, templateMethod);
            i++;
        }
    }

    /**
     * Initialise the render functions from our template.
     * @param renderFunctionString The render function
     * @param staticRenderFnsStrings The static render functions
     */
    @JsOverlay
    public final void initRenderFunctions(Function renderFunctionString,
        Function[] staticRenderFnsStrings)
    {
        this.setRender(renderFunctionString);
        this.setStaticRenderFns(Js.cast(staticRenderFnsStrings));
    }

    /**
     * Initialise the data structure, then set it to either a Factory or directly on the Component.
     * @param useFactory Boolean representing whether or not to use a Factory.
     * @param fieldNames Name of the data fields in the object
     */
    @JsOverlay
    public final void initData(boolean useFactory, String... fieldNames)
    {
        JsPropertyMap<Object> dataFields = JsPropertyMap.of();
        for (String fieldName : fieldNames)
        {
            dataFields.set(fieldName, null);
        }

        if (useFactory)
        {
            String dataFieldsJSON = JSON.stringify(dataFields);
            this.setData((DataFactory) () -> (JsPropertyMap) JSON.parse(dataFieldsJSON));
        }
        else
        {
            this.setData((DataFactory) () -> dataFields);
        }
    }

    /**
     * Add a computed property to this ComponentOptions.
     * If the computed has both a getter and a setter, this will be called twice, once for each.
     * @param javaMethodName Name of the method in the {@link IsVueComponent}
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
     * @param javaMethodName Name of the method in the {@link IsVueComponent}
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

        JsPropertyMap<Object> watchDefinition = JsPropertyMap.of();
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
     * @param exposedTypeName JS name of the type of this property, if not null we will ask Vue to type
     * check based on it
     */
    @JsOverlay
    public final void addJavaProp(String propName, boolean required, String exposedTypeName)
    {
        PropOptions propDefinition = new PropOptions();
        propDefinition.required = required;

        if (exposedTypeName != null)
            propDefinition.type = ((JsPropertyMap<Object>) DomGlobal.window).get(exposedTypeName);

        addProp(propName, propDefinition);
    }

    /**
     * Add a custom prop validator to validate a property
     * @param javaMethodName Name of the method in the {@link IsVueComponent}
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
     * @param javaMethodName Name of the method in the {@link IsVueComponent}
     * @param propertyName The name of the property to validate
     */
    @JsOverlay
    public final void addJavaPropDefaultValue(String javaMethodName, String propertyName)
    {
        PropOptions propDefinition = (PropOptions) props.get(propertyName);
        propDefinition.defaultValue = getJavaComponentMethod(javaMethodName);
    }

    /**
     * Get the given java method from the {@link IsVueComponent}.
     * @param javaMethodName Name of the Java method to retrieve
     * @return The JS function that represent our Java method.
     */
    @JsOverlay
    private Function getJavaComponentMethod(String javaMethodName)
    {
        return (Function) componentExportedTypePrototype.get(javaMethodName);
    }

    /**
     * Return the prototype for our Component Java object. We can use it to get methods from.
     * @return The prototype of our Component Java object
     */
    @JsOverlay
    public final JsPropertyMap<Object> getComponentExportedTypePrototype()
    {
        return componentExportedTypePrototype;
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
    @JsProperty private JsPropertyMap props;

    @JsProperty private JsPropertyMap propsData;
    @JsProperty private JsPropertyMap<ComputedOptions> computed;
    @JsProperty private JsPropertyMap<Function> methods;

    @JsProperty private JsPropertyMap watch;
    @JsProperty private Object el;

    @JsProperty private String template;
    @JsProperty private JsPropertyMap<VueDirectiveOptions> directives;

    @JsProperty private JsPropertyMap<VueComponentOptions> components;
    @JsProperty private IsVueComponent parent;

    @JsProperty private String name;

    @JsProperty private Function render;
    @JsProperty private JsArray<Function> staticRenderFns;

    @JsProperty private JsArray<Object> mixins;

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
    public final JsPropertyMap getProps()
    {
        return props;
    }

    @JsOverlay
    public final VueComponentOptions setProps(JsPropertyMap props)
    {
        this.props = props;
        return this;
    }

    @JsOverlay
    public final VueComponentOptions addProp(String name, PropOptions propOptions)
    {
        if (this.props == null)
            this.props = JsPropertyMap.of();

        ((JsPropertyMap) this.props).set(name, propOptions);
        return this;
    }

    @JsOverlay
    public final JsPropertyMap getPropsData()
    {
        return propsData;
    }

    @JsOverlay
    public final VueComponentOptions setPropsData(JsPropertyMap propsData)
    {
        this.propsData = propsData;
        return this;
    }

    @JsOverlay
    public final JsPropertyMap<ComputedOptions> getComputed()
    {
        return computed;
    }

    @JsOverlay
    public final VueComponentOptions setComputed(JsPropertyMap<ComputedOptions> computed)
    {
        this.computed = computed;
        return this;
    }

    @JsOverlay
    public final VueComponentOptions addComputedOptions(String name, ComputedOptions computed)
    {
        if (this.computed == null)
            this.computed = (JsPropertyMap<ComputedOptions>) new JsObject();

        this.computed.set(name, computed);
        return this;
    }

    @JsOverlay
    public final ComputedOptions getComputedOptions(String name)
    {
        if (this.computed == null)
            this.computed = (JsPropertyMap<ComputedOptions>) new JsObject();

        return this.computed.get(name);
    }

    @JsOverlay
    public final JsPropertyMap<Function> getMethods()
    {
        return methods;
    }

    @JsOverlay
    public final VueComponentOptions setMethods(JsPropertyMap methods)
    {
        this.methods = methods;
        return this;
    }

    @JsOverlay
    public final VueComponentOptions addMethod(String name, Function method)
    {
        if (this.methods == null)
            this.methods = (JsPropertyMap<Function>) new JsObject();

        this.methods.set(name, method);
        return this;
    }

    @JsOverlay
    public final JsPropertyMap getWatch()
    {
        return watch;
    }

    @JsOverlay
    public final VueComponentOptions setWatch(JsPropertyMap watch)
    {
        this.watch = watch;
        return this;
    }

    @JsOverlay
    public final VueComponentOptions addWatch(String name, Object watcher)
    {
        if (this.watch == null)
            this.watch = JsPropertyMap.of();

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
    public final JsArray<Function> getStaticRenderFns()
    {
        return staticRenderFns;
    }

    @JsOverlay
    public final VueComponentOptions setStaticRenderFns(JsArray<Function> staticRenderFns)
    {
        this.staticRenderFns = staticRenderFns;
        return this;
    }

    @JsOverlay
    public final Function getRender()
    {
        return render;
    }

    @JsOverlay
    public final VueComponentOptions setRender(Function render)
    {
        this.render = render;
        return this;
    }

    @JsOverlay
    public final JsPropertyMap<VueDirectiveOptions> getDirectives()
    {
        return directives;
    }

    @JsOverlay
    public final VueComponentOptions setDirectives(JsPropertyMap<VueDirectiveOptions> directives)
    {
        this.directives = directives;
        return this;
    }

    @JsOverlay
    public final JsPropertyMap<VueComponentOptions> getComponents()
    {
        return components;
    }

    @JsOverlay
    public final VueComponentOptions setComponents(JsPropertyMap<VueComponentOptions> components)
    {
        this.components = components;
        return this;
    }

    @JsOverlay
    public final IsVueComponent getParent()
    {
        return parent;
    }

    @JsOverlay
    public final VueComponentOptions setParent(IsVueComponent parent)
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
    public final JsArray<Object> getMixins()
    {
        return mixins;
    }

    @JsOverlay
    public final VueComponentOptions addMixin(Object mixin)
    {
        if (this.mixins == null)
        {
            this.mixins = new JsArray<>();
        }

        this.mixins.push(mixin);
        return this;
    }

    @JsOverlay
    public final VueComponentOptions setMixins(JsArray<Object> mixins)
    {
        this.mixins = mixins;
        return this;
    }
}
