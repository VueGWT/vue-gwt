package com.axellience.vuegwt.tests.client.components.basic.watch.prop;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.annotations.component.Watch;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import com.axellience.vuegwt.tests.client.common.SimpleObject;
import jsinterop.annotations.JsProperty;

@Component
public class WatchPropTestComponent implements IsVueComponent, HasCreated {

  @Prop
  SimpleObject watchedPropAnnotation;

  @JsProperty
  SimpleObject newValueAnnotation;

  @JsProperty
  SimpleObject oldValueAnnotation;

  @JsProperty
  String newValueAnnotationProperty;

  @JsProperty
  String oldValueAnnotationProperty;

  @Prop
  SimpleObject watchedPropDeepAnnotation = null;

  @JsProperty
  String newValueAnnotationDeep;

  @JsProperty
  String oldValueAnnotationDeep;

  @Prop
  String watchedDataImmediateAnnotation;

  @JsProperty
  String newValueAnnotationImmediate;

  @JsProperty
  String oldValueAnnotationImmediate;

  @Prop
  SimpleObject watchedDataPropertyImmediateAnnotation;

  @JsProperty
  String newValueAnnotationPropertyImmediate;

  @JsProperty
  String oldValueAnnotationPropertyImmediate;

  @Prop
  SimpleObject watchedPropWatchMethod;

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
}
