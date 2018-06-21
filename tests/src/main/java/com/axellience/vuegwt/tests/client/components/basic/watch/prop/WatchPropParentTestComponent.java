package com.axellience.vuegwt.tests.client.components.basic.watch.prop;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.tests.client.common.SimpleObject;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

@Component(components = WatchPropTestComponent.class)
public class WatchPropParentTestComponent implements IsVueComponent {

  @JsProperty
  SimpleObject watchedPropAnnotation = null;

  @JsProperty
  SimpleObject watchedPropDeepAnnotation = null;

  @JsProperty
  String watchedDataImmediateAnnotation = "initialValue";

  @JsProperty
  SimpleObject watchedDataPropertyImmediateAnnotation = new SimpleObject("initialValue");

  @JsProperty
  SimpleObject watchedProp$WatchString = null;

  @JsProperty
  SimpleObject watchedProp$WatchMethod = null;

  @JsMethod
  public void changeWatchedPropAnnotation(String property) {
    if (property == null) {
      watchedPropAnnotation = null;
    } else {
      watchedPropAnnotation = new SimpleObject();
      watchedPropAnnotation.setStringProperty(property);
    }
  }

  @JsMethod
  public void changeWatchedPropDeepAnnotation(String property) {
    if (property == null) {
      watchedPropDeepAnnotation = null;
    } else {
      watchedPropDeepAnnotation = new SimpleObject();
      watchedPropDeepAnnotation.setStringProperty(property);
    }
  }

  @JsMethod
  public void changeWatchedProp$WatchString(String property) {
    if (property == null) {
      watchedProp$WatchString = null;
    } else {
      watchedProp$WatchString = new SimpleObject();
      watchedProp$WatchString.setStringProperty(property);
    }
  }

  @JsMethod
  public void changeWatchedProp$WatchMethod(String property) {
    if (property == null) {
      watchedProp$WatchMethod = null;
    } else {
      watchedProp$WatchMethod = new SimpleObject();
      watchedProp$WatchMethod.setStringProperty(property);
    }
  }

  @JsMethod
  public WatchPropTestComponent getWatchPropTestComponent() {
    return vue().$ref("watchPropTest");
  }
}
