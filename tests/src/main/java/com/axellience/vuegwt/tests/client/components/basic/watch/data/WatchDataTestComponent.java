package com.axellience.vuegwt.tests.client.components.basic.watch.data;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.annotations.component.Watch;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import com.axellience.vuegwt.tests.client.common.SimpleObject;
import jsinterop.annotations.JsMethod;

@Component
public class WatchDataTestComponent implements IsVueComponent, HasCreated {

  @Data
  SimpleObject watchedDataAnnotation = null;

  SimpleObject newValueAnnotation;

  SimpleObject oldValueAnnotation;

  String newValueAnnotationProperty;

  String oldValueAnnotationProperty;

  @Data
  SimpleObject watchedDataDeepAnnotation = null;

  String newValueAnnotationDeep;

  String oldValueAnnotationDeep;

  @Data
  String watchedDataImmediateAnnotation = "initialValue";

  String newValueAnnotationImmediate;

  String oldValueAnnotationImmediate;

  @Data
  SimpleObject watchedDataPropertyImmediateAnnotation = new SimpleObject("initialValue");

  String newValueAnnotationPropertyImmediate;

  String oldValueAnnotationPropertyImmediate;

  @Data
  SimpleObject watchedData$WatchMethod = null;

  SimpleObject newValue$WatchMethod;

  SimpleObject oldValue$WatchMethod;

  String newValue$WatchMethodProperty;

  String oldValue$WatchMethodProperty;

  @Override
  public void created() {
    vue().$watch(() -> watchedData$WatchMethod, (newValue, oldValue) -> {
      this.newValue$WatchMethod = newValue;
      this.oldValue$WatchMethod = oldValue;
    });
    vue().$watch(
        () -> {
          if (watchedData$WatchMethod == null) {
            return null;
          }
          return watchedData$WatchMethod.getStringProperty();
        },
        (newValue, oldValue) -> {
          this.newValue$WatchMethodProperty = newValue;
          this.oldValue$WatchMethodProperty = oldValue;
        }
    );
  }

  @Watch("watchedDataAnnotation")
  public void onWatchedDataChange(SimpleObject newValue, SimpleObject oldValue) {
    this.newValueAnnotation = newValue;
    this.oldValueAnnotation = oldValue;
  }

  @Watch("watchedDataAnnotation.stringProperty")
  public void onWatchedDataPropertyChange(String newValue, String oldValue) {
    this.newValueAnnotationProperty = newValue;
    this.oldValueAnnotationProperty = oldValue;
  }

  @Watch(value = "watchedDataDeepAnnotation", isDeep = true)
  public void onWatchedDataDeepChange(SimpleObject newValue, SimpleObject oldValue) {
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
  public void changeWatchedDataAnnotation(String property) {
    if (property == null) {
      watchedDataAnnotation = null;
    } else {
      watchedDataAnnotation = new SimpleObject();
      watchedDataAnnotation.setStringProperty(property);
    }
  }

  @JsMethod
  public void changeWatchedDataDeepAnnotation(String property) {
    if (property == null) {
      watchedDataDeepAnnotation = null;
    } else {
      watchedDataDeepAnnotation = new SimpleObject();
      watchedDataDeepAnnotation.setStringProperty(property);
    }
  }

  @JsMethod
  public void changeWatchedData$WatchMethod(String property) {
    if (property == null) {
      watchedData$WatchMethod = null;
    } else {
      watchedData$WatchMethod = new SimpleObject();
      watchedData$WatchMethod.setStringProperty(property);
    }
  }

  @JsMethod
  public SimpleObject getWatchedDataAnnotation() {
    return watchedDataAnnotation;
  }

  @JsMethod
  public SimpleObject getWatchedDataDeepAnnotation() {
    return watchedDataDeepAnnotation;
  }

  @JsMethod
  public String getWatchedDataImmediateAnnotation() {
    return watchedDataImmediateAnnotation;
  }

  @JsMethod
  public SimpleObject getWatchedDataPropertyImmediateAnnotation() {
    return watchedDataPropertyImmediateAnnotation;
  }

  @JsMethod
  public SimpleObject getWatchedData$WatchMethod() {
    return watchedData$WatchMethod;
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
