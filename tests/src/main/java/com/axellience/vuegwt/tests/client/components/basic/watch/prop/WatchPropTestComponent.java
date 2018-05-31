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
  @JsProperty
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
  @JsProperty
  SimpleObject watchedPropDeepAnnotation = null;

  @JsProperty
  String newValueAnnotationDeep;

  @JsProperty
  String oldValueAnnotationDeep;

  @Prop
  @JsProperty
  SimpleObject watchedPropWatchString;

  @JsProperty
  SimpleObject newValue$WatchString;

  @JsProperty
  SimpleObject oldValue$WatchString;

  @JsProperty
  String newValue$WatchStringProperty;

  @JsProperty
  String oldValue$WatchStringProperty;

  @Prop
  @JsProperty
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
    vue().$watch("watchedPropWatchString", (newValue, oldValue) -> {
      this.newValue$WatchString = (SimpleObject) newValue;
      this.oldValue$WatchString = (SimpleObject) oldValue;
    });
    vue().$watch("watchedPropWatchString.stringProperty", (newValue, oldValue) -> {
      this.newValue$WatchStringProperty = (String) newValue;
      this.oldValue$WatchStringProperty = (String) oldValue;
    });

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
  public void onwatchedPropChange(SimpleObject newValue, SimpleObject oldValue) {
    this.newValueAnnotation = newValue;
    this.oldValueAnnotation = oldValue;
  }

  @Watch("watchedPropAnnotation.stringProperty")
  public void onwatchedPropPropertyChange(String newValue, String oldValue) {
    this.newValueAnnotationProperty = newValue;
    this.oldValueAnnotationProperty = oldValue;
  }

  @Watch(value = "watchedPropDeepAnnotation", isDeep = true)
  public void onWatchedPropDeepChange(SimpleObject newValue, SimpleObject oldValue) {
    this.newValueAnnotationDeep = newValue != null ? newValue.getStringProperty() : null;
    this.oldValueAnnotationDeep = oldValue != null ? oldValue.getStringProperty() : null;
  }
}
