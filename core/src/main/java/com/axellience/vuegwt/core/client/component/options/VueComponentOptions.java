package com.axellience.vuegwt.core.client.component.options;

import static com.axellience.vuegwt.core.client.tools.VueGWTTools.proxyField;
import static elemental2.core.Global.JSON;
import static jsinterop.base.Js.cast;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.options.computed.ComputedKind;
import com.axellience.vuegwt.core.client.component.options.computed.ComputedOptions;
import com.axellience.vuegwt.core.client.component.options.data.DataFactory;
import com.axellience.vuegwt.core.client.component.options.props.PropOptions;
import com.axellience.vuegwt.core.client.component.options.props.PropProxyDefinition;
import com.axellience.vuegwt.core.client.directive.options.VueDirectiveOptions;
import elemental2.core.Function;
import elemental2.core.JsArray;
import elemental2.dom.DomGlobal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Provider;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.JsPropertyMap;

/**
 * Java representation of VueComponentOptions Class extending this one are generated using the
 * Annotation processor for each VueComponent
 * <p>
 * An instance of this Class can be immediately passed to Vue.js instance where it's expecting a
 * component options object.
 * <p>
 * This is an internal Class, it shouldn't be extended in applications that use Vue GWT.
 *
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class VueComponentOptions<T extends IsVueComponent> implements JsPropertyMap {

  private JsPropertyMap<Object> componentExportedTypePrototype;
  private Map<String, Provider<?>> dependenciesProvider;
  private Set<String> dataFieldsToProxy;
  private Set<PropProxyDefinition> propsToProxy;

  /**
   * Set the JS Prototype of the ExportedType Java Class represented by this {@link
   * VueComponentOptions}. This prototype will be used to retrieve the java methods of our {@link
   * IsVueComponent}.
   *
   * @param prototype The JS prototype of the ExportedType Java Class
   */
  @JsOverlay
  public final void setComponentExportedTypePrototype(JsPropertyMap<Object> prototype) {
    this.componentExportedTypePrototype = prototype;
    // This must be set for Components extending Native JS Components
    this.componentExportedTypePrototype.set("options", this);
  }

  /**
   * Initialise the render functions from our template.
   *
   * @param renderFunctionString The render function
   * @param staticRenderFnsStrings The static render functions
   */
  @JsOverlay
  public final void initRenderFunctions(Function renderFunctionString,
      Function[] staticRenderFnsStrings) {
    this.setRender(renderFunctionString);
    this.setStaticRenderFns(cast(staticRenderFnsStrings));
  }

  /**
   * Initialise the data structure, then set it to either a Factory or directly on the Component.
   *
   * @param useFactory Boolean representing whether or not to use a Factory.
   * @param fieldNames Name of the data fields in the object
   */
  @JsOverlay
  public final void initData(boolean useFactory, Set<String> fieldNames) {
    JsPropertyMap<Object> dataFields = JsPropertyMap.of();
    dataFieldsToProxy = new HashSet<>();
    for (String fieldName : fieldNames) {
      dataFields.set(fieldName, null);

      // If field name starts with $ or _ Vue.js won't proxify it so we must do it
      if (fieldName.startsWith("$") || fieldName.startsWith("_")) {
        dataFieldsToProxy.add(fieldName);
      }
    }

    if (useFactory) {
      String dataFieldsJSON = JSON.stringify(dataFields);
      this.setData((DataFactory) () -> JSON.parse(dataFieldsJSON));
    } else {
      this.setData((DataFactory) () -> dataFields);
    }
  }

  /**
   * Add a computed property to this ComponentOptions. If the computed has both a getter and a
   * setter, this will be called twice, once for each.
   *
   * @param javaMethod Function pointer to the method in the {@link IsVueComponent}
   * @param computedPropertyName Name of the computed property in the Template and the
   * ComponentOptions
   * @param kind Kind of the computed method (getter or setter)
   */
  @JsOverlay
  public final void addJavaComputed(Function javaMethod, String computedPropertyName,
      ComputedKind kind) {
    ComputedOptions computedDefinition = getComputedOptions(computedPropertyName);
    if (computedDefinition == null) {
      computedDefinition = new ComputedOptions();
      addComputedOptions(computedPropertyName, computedDefinition);
    }

    if (kind == ComputedKind.GETTER) {
      computedDefinition.get = javaMethod;
    } else if (kind == ComputedKind.SETTER) {
      computedDefinition.set = javaMethod;
    }
  }

  /**
   * Add a watch property to this Component Definition
   *
   * @param javaMethod Function pointer to the method in the {@link IsVueComponent}
   * @param watchedPropertyName Name of the property name to watch in the data model
   * @param isDeep Is the watcher deep (will watch child properties)
   * @param isImmediate Is the watcher immediate (will trigger on initial value)
   */
  @JsOverlay
  public final void addJavaWatch(Function javaMethod, String watchedPropertyName,
      boolean isDeep, boolean isImmediate) {
    if (!isDeep && !isImmediate) {
      addWatch(watchedPropertyName, javaMethod);
      return;
    }

    JsPropertyMap<Object> watchDefinition = JsPropertyMap.of();
    watchDefinition.set("handler", javaMethod);
    watchDefinition.set("deep", isDeep);
    watchDefinition.set("immediate", isImmediate);
    addWatch(watchedPropertyName, watchDefinition);
  }

  /**
   * Add the given lifecycle hook to the {@link VueComponentOptions}.
   *
   * @param hookName Name of the hook to add
   * @param javaMethod Function pointer to the method in the {@link IsVueComponent}
   */
  @JsOverlay
  public final void addHookMethod(String hookName, Function javaMethod) {
    set(hookName, javaMethod);
  }

  /**
   * Add a prop to our ComponentOptions. This will allow to receive data from the outside of our
   * Component.
   *
   * @param propName The name of the prop
   * @param fieldName The name of the java field for that prop
   * @param required Is the property required (mandatory)
   * @param exposedTypeName JS name of the type of this property, if not null we will ask Vue to
   * type check based on it
   */
  @JsOverlay
  public final void addJavaProp(String propName, String fieldName, boolean required,
      String exposedTypeName) {
    if (propsToProxy == null) {
      propsToProxy = new HashSet<>();
    }

    PropOptions propDefinition = new PropOptions();
    propDefinition.required = required;

    if (exposedTypeName != null) {
      propDefinition.type = ((JsPropertyMap<Object>) DomGlobal.window).get(exposedTypeName);
    }

    propsToProxy.add(new PropProxyDefinition(propName, fieldName));
    addProp(propName, propDefinition);
  }

  /**
   * Add a custom prop validator to validate a property
   *
   * @param javaMethod Function pointer to the method in the {@link IsVueComponent}
   * @param propertyName The name of the property to validate
   */
  @JsOverlay
  public final void addJavaPropValidator(Function javaMethod, String propertyName) {
    PropOptions propDefinition = getProps().get(propertyName);
    propDefinition.validator = javaMethod;
  }

  /**
   * Add a custom prop validator to validate a property
   *
   * @param javaMethod Function pointer to the method in the {@link IsVueComponent}
   * @param propertyName The name of the property to validate
   */
  @JsOverlay
  public final void addJavaPropDefaultValue(Function javaMethod, String propertyName) {
    PropOptions propDefinition = getProps().get(propertyName);
    propDefinition.defaultValue = javaMethod;
  }

  @JsOverlay
  public final void registerTemplateMethods(Function... templateMethods) {
    int count = 0;
    for (Function templateMethod : templateMethods) {
      this.addMethod("exp$" + count, templateMethod);
      count++;
    }
  }

  /**
   * Return the prototype for our Component Java object. We can use it to get methods from.
   *
   * @return The prototype of our Component Java object
   */
  @JsOverlay
  public final JsPropertyMap<Object> getComponentExportedTypePrototype() {
    return componentExportedTypePrototype;
  }

  @JsOverlay
  public final Provider<?> getProvider(Class<T> component) {
    return getProviders().get(component.getCanonicalName());
  }

  @JsOverlay
  public final void addProvider(Class<T> component, Provider<?> dependenciesProvider) {
    getProviders().put(component.getCanonicalName(), dependenciesProvider);
  }

  @JsOverlay
  public final void addAllProviders(Map<String, Provider<?>> providers) {
    getProviders().putAll(providers);
  }

  @JsOverlay
  public final Map<String, Provider<?>> getProviders() {
    if (this.dependenciesProvider == null) {
      this.dependenciesProvider = new HashMap<>();
    }

    return dependenciesProvider;
  }

  @JsOverlay
  public final void proxyFields(IsVueComponent instance) {
    if (dataFieldsToProxy != null && !dataFieldsToProxy.isEmpty()) {
      for (String dataField : dataFieldsToProxy) {
        proxyField(instance, "_data", dataField, dataField);
      }
    }

    if (propsToProxy != null && !propsToProxy.isEmpty()) {
      for (PropProxyDefinition def : propsToProxy) {
        proxyField(instance, "_props", def.getPropName(), def.getFieldName());
      }
    }
  }

  /*
   * ---------------------------------------------
   *
   * Instance Properties and Methods
   *
   * ---------------------------------------------
   */
  @JsOverlay
  public final Object getData() {
    return get("data");
  }

  @JsOverlay
  public final VueComponentOptions setData(Object data) {
    set("data", data);
    return this;
  }

  @JsOverlay
  public final JsPropertyMap<PropOptions> getProps() {
    return cast(get("props"));
  }

  @JsOverlay
  public final VueComponentOptions setProps(JsPropertyMap<PropOptions> props) {
    set("props", props);
    return this;
  }

  @JsOverlay
  public final VueComponentOptions addProp(String name, PropOptions propOptions) {
    if (getProps() == null) {
      setProps(cast(JsPropertyMap.of()));
    }

    getProps().set(name, propOptions);
    return this;
  }

  @JsOverlay
  public final JsPropertyMap getPropsData() {
    return (JsPropertyMap) get("propsData");
  }

  @JsOverlay
  public final VueComponentOptions setPropsData(JsPropertyMap propsData) {
    set("propsData", propsData);
    return this;
  }

  @JsOverlay
  public final JsPropertyMap<ComputedOptions> getComputed() {
    return cast(get("computed"));
  }

  @JsOverlay
  public final VueComponentOptions setComputed(JsPropertyMap<ComputedOptions> computed) {
    set("computed", computed);
    return this;
  }

  @JsOverlay
  public final VueComponentOptions addComputedOptions(String name, ComputedOptions computed) {
    if (getComputed() == null) {
      setComputed(cast(JsPropertyMap.of()));
    }

    getComputed().set(name, computed);
    return this;
  }

  @JsOverlay
  public final ComputedOptions getComputedOptions(String name) {
    if (getComputed() == null) {
      setComputed(cast(JsPropertyMap.of()));
    }

    return getComputed().get(name);
  }

  @JsOverlay
  public final JsPropertyMap<Function> getMethods() {
    return cast(get("methods"));
  }

  @JsOverlay
  public final VueComponentOptions setMethods(JsPropertyMap<Function> methods) {
    set("methods", methods);
    return this;
  }

  @JsOverlay
  public final VueComponentOptions addMethod(String name, Function method) {
    if (getMethods() == null) {
      setMethods(cast(JsPropertyMap.of()));
    }

    getMethods().set(name, method);
    return this;
  }

  @JsOverlay
  public final JsPropertyMap getWatch() {
    return (JsPropertyMap) get("watch");
  }

  @JsOverlay
  public final VueComponentOptions setWatch(JsPropertyMap watch) {
    set("watch", watch);
    return this;
  }

  @JsOverlay
  public final VueComponentOptions addWatch(String name, Object watcher) {
    if (getWatch() == null) {
      setWatch(JsPropertyMap.of());
    }

    getWatch().set(name, watcher);
    return this;
  }

  @JsOverlay
  public final Object getEl() {
    return get("el");
  }

  @JsOverlay
  public final void setEl(Object el) {
    set("el", el);
  }

  @JsOverlay
  public final String getTemplate() {
    return (String) get("template");
  }

  @JsOverlay
  public final VueComponentOptions setTemplate(String template) {
    set("template", template);
    return this;
  }

  @JsOverlay
  public final JsArray<Function> getStaticRenderFns() {
    return cast(get("staticRenderFns"));
  }

  @JsOverlay
  public final VueComponentOptions setStaticRenderFns(JsArray<Function> staticRenderFns) {
    set("staticRenderFns", staticRenderFns);
    return this;
  }

  @JsOverlay
  public final Function getRender() {
    return (Function) get("render");
  }

  @JsOverlay
  public final VueComponentOptions setRender(Function render) {
    set("render", render);
    return this;
  }

  @JsOverlay
  public final JsPropertyMap<VueDirectiveOptions> getDirectives() {
    return cast(get("directives"));
  }

  @JsOverlay
  public final VueComponentOptions setDirectives(JsPropertyMap<VueDirectiveOptions> directives) {
    set("directives", directives);
    return this;
  }

  @JsOverlay
  public final JsPropertyMap<VueComponentOptions> getComponents() {
    return cast(get("components"));
  }

  @JsOverlay
  public final VueComponentOptions setComponents(JsPropertyMap<VueComponentOptions> components) {
    set("components", components);
    return this;
  }

  @JsOverlay
  public final IsVueComponent getParent() {
    return (IsVueComponent) get("parent");
  }

  @JsOverlay
  public final VueComponentOptions setParent(IsVueComponent parent) {
    set("parent", parent);
    return this;
  }

  @JsOverlay
  public final String getName() {
    return (String) get("name");
  }

  @JsOverlay
  public final VueComponentOptions setName(String name) {
    set("name", name);
    return this;
  }

  @JsOverlay
  public final JsArray<Object> getMixins() {
    return cast(get("mixins"));
  }

  @JsOverlay
  public final VueComponentOptions addMixin(Object mixin) {
    if (getMixins() == null) {
      setMixins(new JsArray<>());
    }

    getMixins().push(mixin);
    return this;
  }

  @JsOverlay
  public final VueComponentOptions setMixins(JsArray<Object> mixins) {
    set("mixins", mixins);
    return this;
  }
}
