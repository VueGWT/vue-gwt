package com.axellience.vuegwt.tests.client.components.basic.watch.data;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Watch;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import com.axellience.vuegwt.tests.client.common.SimpleObject;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

@Component
public class WatchDataTestComponent implements IsVueComponent, HasCreated {

  @JsProperty
  SimpleObject watchedDataAnnotation = null;

  @JsProperty
  SimpleObject newValueAnnotation;

  @JsProperty
  SimpleObject oldValueAnnotation;

  @JsProperty
  String newValueAnnotationProperty;

  @JsProperty
  String oldValueAnnotationProperty;

  @JsProperty
  SimpleObject watchedDataDeepAnnotation = null;

  @JsProperty
  String newValueAnnotationDeep;

  @JsProperty
  String oldValueAnnotationDeep;

  @JsProperty
  String watchedDataImmediateAnnotation = "initialValue";

  @JsProperty
  String newValueAnnotationImmediate;

  @JsProperty
  String oldValueAnnotationImmediate;

  @JsProperty
  SimpleObject watchedDataPropertyImmediateAnnotation = new SimpleObject("initialValue");

  @JsProperty
  String newValueAnnotationPropertyImmediate;

  @JsProperty
  String oldValueAnnotationPropertyImmediate;

  @JsProperty
  SimpleObject watchedData$WatchString = null;

  @JsProperty
  SimpleObject newValue$WatchString;

  @JsProperty
  SimpleObject oldValue$WatchString;

  @JsProperty
  String newValue$WatchStringProperty;

  @JsProperty
  String oldValue$WatchStringProperty;

  @JsProperty
  SimpleObject watchedData$WatchMethod = null;

  @JsProperty
  SimpleObject newValue$WatchMethod;

  @JsProperty
  SimpleObject oldValue$WatchMethod;

  @JsProperty
  String newValue$WatchMethodProperty;

  @JsProperty
  String oldValue$WatchMethodProperty;

  @Override
  public void created() {
    vue().$watch("watchedData$WatchString", (newValue, oldValue) -> {
      this.newValue$WatchString = (SimpleObject) newValue;
      this.oldValue$WatchString = (SimpleObject) oldValue;
    });
    vue().$watch("watchedData$WatchString.stringProperty", (newValue, oldValue) -> {
      this.newValue$WatchStringProperty = (String) newValue;
      this.oldValue$WatchStringProperty = (String) oldValue;
    });

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
  public void changeWatchedData$WatchString(String property) {
    if (property == null) {
      watchedData$WatchString = null;
    } else {
      watchedData$WatchString = new SimpleObject();
      watchedData$WatchString.setStringProperty(property);
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
}
