package com.axellience.vuegwt.core.client.vnode;

import com.axellience.vuegwt.core.client.component.options.functions.OnEvent;
import elemental2.core.JsArray;
import elemental2.core.JsObject;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.JsPropertyMap;

/**
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class VNodeData {

  @JsProperty
  protected Object key;
  @JsProperty
  protected String slot;
  @JsProperty
  protected JsPropertyMap<ScopedSlot> scopedSlots;
  @JsProperty
  protected String ref;
  @JsProperty
  protected String tag;
  @JsProperty
  protected String staticClass;
  @JsProperty(name = "class")
  protected Object classProp;
  @JsProperty
  protected JsPropertyMap staticStyle;
  @JsProperty
  protected Object style;
  @JsProperty
  protected JsPropertyMap props;
  @JsProperty
  protected JsPropertyMap attrs;
  @JsProperty
  protected JsPropertyMap domProps;
  @JsProperty
  protected JsPropertyMap hook;
  @JsProperty
  protected Object on;
  @JsProperty
  protected Object nativeOn;
  @JsProperty
  protected Object transition;
  @JsProperty
  protected boolean show;
  @JsProperty
  protected InlineTemplate inlineTemplate;
  @JsProperty
  protected JsArray<VNodeDirective> directives;
  @JsProperty
  protected boolean keepAlive;

  public VNodeData() {
  }

  @JsOverlay
  public static VNodeData get() {
    return new VNodeData();
  }

  @JsOverlay
  public final <T> T getKey() {
    return (T) key;
  }

  @JsOverlay
  public final VNodeData setKey(String key) {
    this.key = key;
    return this;
  }

  @JsOverlay
  public final VNodeData setKey(int key) {
    this.key = key;
    return this;
  }

  @JsOverlay
  public final VNodeData setKey(float key) {
    this.key = key;
    return this;
  }

  @JsOverlay
  public final String getSlot() {
    return slot;
  }

  @JsOverlay
  public final VNodeData setSlot(String slot) {
    this.slot = slot;
    return this;
  }

  @JsOverlay
  public final JsPropertyMap<ScopedSlot> getScopedSlots() {
    return scopedSlots;
  }

  @JsOverlay
  public final VNodeData setScopedSlots(JsPropertyMap<ScopedSlot> scopedSlots) {
    this.scopedSlots = scopedSlots;
    return this;
  }

  @JsOverlay
  public final VNodeData scopedSlot(String name, ScopedSlot scopedSlot) {
    if (this.scopedSlots == null) {
      this.scopedSlots = (JsPropertyMap<ScopedSlot>) new JsObject();
    }

    this.scopedSlots.set(name, scopedSlot);
    return this;
  }

  @JsOverlay
  public final String getRef() {
    return ref;
  }

  @JsOverlay
  public final VNodeData setRef(String ref) {
    this.ref = ref;
    return this;
  }

  @JsOverlay
  public final String getTag() {
    return tag;
  }

  @JsOverlay
  public final VNodeData setTag(String tag) {
    this.tag = tag;
    return this;
  }

  @JsOverlay
  public final String getStaticClass() {
    return staticClass;
  }

  @JsOverlay
  public final VNodeData setStaticClass(String staticClass) {
    this.staticClass = staticClass;
    return this;
  }

  @JsOverlay
  public final Object getClassProp() {
    return classProp;
  }

  @JsOverlay
  public final VNodeData setClassProp(Object classProp) {
    this.classProp = classProp;
    return this;
  }

  @JsOverlay
  public final JsPropertyMap getStaticStyle() {
    return staticStyle;
  }

  @JsOverlay
  public final VNodeData addStaticStyle(String name, Object value) {
    if (this.staticStyle == null) {
      this.staticStyle = JsPropertyMap.of();
    }

    this.staticStyle.set(name, value);
    return this;
  }

  @JsOverlay
  public final VNodeData setStaticStyle(JsPropertyMap staticStyle) {
    this.staticStyle = staticStyle;
    return this;
  }

  @JsOverlay
  public final <T> T getStyle() {
    return (T) style;
  }

  @JsOverlay
  public final VNodeData setStyle(JsPropertyMap style) {
    this.style = style;
    return this;
  }

  @JsOverlay
  public final VNodeData setStyle(JsArray style) {
    this.style = style;
    return this;
  }

  @JsOverlay
  public final JsPropertyMap getProps() {
    return props;
  }

  @JsOverlay
  public final VNodeData setProps(JsPropertyMap props) {
    this.props = props;
    return this;
  }

  @JsOverlay
  public final VNodeData prop(String name, Object prop) {
    if (this.props == null) {
      this.props = JsPropertyMap.of();
    }

    this.props.set(name, prop);
    return this;
  }

  @JsOverlay
  public final JsPropertyMap getAttrs() {
    return attrs;
  }

  @JsOverlay
  public final VNodeData setAttrs(JsPropertyMap attrs) {
    this.attrs = attrs;
    return this;
  }

  @JsOverlay
  public final VNodeData attr(String name, Object attr) {
    if (this.attrs == null) {
      this.attrs = JsPropertyMap.of();
    }

    this.attrs.set(name, attr);
    return this;
  }

  @JsOverlay
  public final JsPropertyMap getDomProps() {
    return domProps;
  }

  @JsOverlay
  public final VNodeData setDomProps(JsPropertyMap domProps) {
    this.domProps = domProps;
    return this;
  }

  @JsOverlay
  public final VNodeData domProp(String name, Object domProp) {
    if (this.domProps == null) {
      this.domProps = JsPropertyMap.of();
    }

    this.domProps.set(name, domProp);
    return this;
  }

  @JsOverlay
  public final JsPropertyMap getHook() {
    return hook;
  }

  @JsOverlay
  public final VNodeData setHook(JsPropertyMap hook) {
    this.hook = hook;
    return this;
  }

  @JsOverlay
  public final VNodeData hook(String name, Object hook) {
    if (this.hook == null) {
      this.hook = JsPropertyMap.of();
    }

    this.hook.set(name, hook);
    return this;
  }

  @JsOverlay
  public final <T> T getOn() {
    return (T) on;
  }

  @JsOverlay
  public final VNodeData setOn(Object on) {
    this.on = on;
    return this;
  }

  @JsOverlay
  public final VNodeData on(String name, OnEvent on) {
    if (this.on == null) {
      this.on = JsPropertyMap.of();
    }

    ((JsPropertyMap) this.on).set(name, on);
    return this;
  }

  @JsOverlay
  public final Object getNativeOn() {
    return nativeOn;
  }

  @JsOverlay
  public final VNodeData setNativeOn(Object nativeOn) {
    this.nativeOn = nativeOn;
    return this;
  }

  @JsOverlay
  public final VNodeData nativeOn(String name, OnEvent nativeOn) {
    if (this.nativeOn == null) {
      this.nativeOn = JsPropertyMap.of();
    }

    ((JsPropertyMap) this.nativeOn).set(name, nativeOn);
    return this;
  }

  @JsOverlay
  public final Object getTransition() {
    return transition;
  }

  @JsOverlay
  public final VNodeData setTransition(Object transition) {
    this.transition = transition;
    return this;
  }

  @JsOverlay
  public final boolean isShow() {
    return show;
  }

  @JsOverlay
  public final VNodeData setShow(boolean show) {
    this.show = show;
    return this;
  }

  @JsOverlay
  public final InlineTemplate getInlineTemplate() {
    return inlineTemplate;
  }

  @JsOverlay
  public final VNodeData setInlineTemplate(InlineTemplate inlineTemplate) {
    this.inlineTemplate = inlineTemplate;
    return this;
  }

  @JsOverlay
  public final JsArray<VNodeDirective> getDirectives() {
    return directives;
  }

  @JsOverlay
  public final VNodeData setDirectives(JsArray<VNodeDirective> directives) {
    this.directives = directives;
    return this;
  }

  @JsOverlay
  public final VNodeData addDirective(VNodeDirective directive) {
    if (this.directives == null) {
      this.directives = new JsArray<>();
    }

    this.directives.push(directive);
    return this;
  }

  @JsOverlay
  public final boolean isKeepAlive() {
    return keepAlive;
  }

  @JsOverlay
  public final VNodeData setKeepAlive(boolean keepAlive) {
    this.keepAlive = keepAlive;
    return this;
  }
}
