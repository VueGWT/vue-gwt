package com.axellience.vuegwt.testsapp.client.components.basic.watch.prop;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.annotations.component.Watch;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import com.axellience.vuegwt.testsapp.client.common.SimpleObject;
import jsinterop.annotations.JsMethod;

@Component
public class WatchPropTestComponent implements IsVueComponent, HasCreated {

  @Prop
  SimpleObject watchedPropAnnotation;

  SimpleObject newValueAnnotation;

  SimpleObject oldValueAnnotation;

  String newValueAnnotationProperty;

  String oldValueAnnotationProperty;

  @Prop
  SimpleObject watchedPropDeepAnnotation = null;

  String newValueAnnotationDeep;

  String oldValueAnnotationDeep;

  @Prop
  String watchedDataImmediateAnnotation;

  String newValueAnnotationImmediate;

  String oldValueAnnotationImmediate;

  @Prop
  SimpleObject watchedDataPropertyImmediateAnnotation;

  String newValueAnnotationPropertyImmediate;

  String oldValueAnnotationPropertyImmediate;

  @Prop
  SimpleObject watchedPropWatchMethod;

  SimpleObject newValue$WatchMethod;

  SimpleObject oldValue$WatchMethod;

  String newValue$WatchMethodProperty;

  String oldValue$WatchMethodProperty;

  @Override
  public void created() {
    vue().$watch(() -> watchedPropWatchMethod, (newValue, oldValue) -> {
      this.newValue$WatchMethod = newValue;
      this.oldValue$WatchMethod = oldValue;
    });
    vue().$watch(
        () -> {
          if (watchedPropWatchMethod == null) {
            return null;
          }
          return watchedPropWatchMethod.getStringProperty();
        },
        (newValue, oldValue) -> {
          this.newValue$WatchMethodProperty = newValue;
          this.oldValue$WatchMethodProperty = oldValue;
        }
    );
  }

  @Watch("watchedPropAnnotation")
  public void onWatchedPropChange(SimpleObject newValue, SimpleObject oldValue) {
    this.newValueAnnotation = newValue;
    this.oldValueAnnotation = oldValue;
  }

  @Watch("watchedPropAnnotation.stringProperty")
  public void onWatchedPropPropertyChange(String newValue, String oldValue) {
    this.newValueAnnotationProperty = newValue;
    this.oldValueAnnotationProperty = oldValue;
  }

  @Watch(value = "watchedPropDeepAnnotation", isDeep = true)
  public void onWatchedPropDeepChange(SimpleObject newValue, SimpleObject oldValue) {
    this.newValueAnnotationDeep = newValue != null ? newValue.getStringProperty() : null;
    this.oldValueAnnotationDeep = oldValue != null ? oldValue.getStringProperty() : null;
  }

  @Watch(value = "watchedDataImmediateAnnotation", isImmediate = true)
  public void onWatchedDataImmediateChange(String newValue, String oldValue) {
    this.newValueAnnotationImmediate = newValue;
    this.oldValueAnnotationImmediate = oldValue;
  }

  @Watch(value = "watchedDataPropertyImmediateAnnotation.stringProperty", isImmediate = true)
  public void onWatchedDataPropertyImmediateChange(String newValue, String oldValue) {
    this.newValueAnnotationPropertyImmediate = newValue;
    this.oldValueAnnotationPropertyImmediate = oldValue;
  }

  @JsMethod
  public SimpleObject getNewValueAnnotation() {
    return newValueAnnotation;
  }

  @JsMethod
  public SimpleObject getOldValueAnnotation() {
    return oldValueAnnotation;
  }

  @JsMethod
  public String getNewValueAnnotationProperty() {
    return newValueAnnotationProperty;
  }

  @JsMethod
  public String getOldValueAnnotationProperty() {
    return oldValueAnnotationProperty;
  }

  @JsMethod
  public String getNewValueAnnotationDeep() {
    return newValueAnnotationDeep;
  }

  @JsMethod
  public String getOldValueAnnotationDeep() {
    return oldValueAnnotationDeep;
  }

  @JsMethod
  public String getNewValueAnnotationImmediate() {
    return newValueAnnotationImmediate;
  }

  @JsMethod
  public String getOldValueAnnotationImmediate() {
    return oldValueAnnotationImmediate;
  }

  @JsMethod
  public String getNewValueAnnotationPropertyImmediate() {
    return newValueAnnotationPropertyImmediate;
  }

  @JsMethod
  public String getOldValueAnnotationPropertyImmediate() {
    return oldValueAnnotationPropertyImmediate;
  }

  @JsMethod
  public SimpleObject getNewValue$WatchMethod() {
    return newValue$WatchMethod;
  }

  @JsMethod
  public SimpleObject getOldValue$WatchMethod() {
    return oldValue$WatchMethod;
  }

  @JsMethod
  public String getNewValue$WatchMethodProperty() {
    return newValue$WatchMethodProperty;
  }

  @JsMethod
  public String getOldValue$WatchMethodProperty() {
    return oldValue$WatchMethodProperty;
  }
}
