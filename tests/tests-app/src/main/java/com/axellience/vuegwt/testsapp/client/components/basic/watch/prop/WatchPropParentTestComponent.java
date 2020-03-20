package com.axellience.vuegwt.testsapp.client.components.basic.watch.prop;

import static jsinterop.base.Js.uncheckedCast;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.testsapp.client.common.SimpleObject;
import jsinterop.annotations.JsMethod;

@Component(components = WatchPropTestComponent.class)
public class WatchPropParentTestComponent implements IsVueComponent {

  @Data
  SimpleObject watchedPropAnnotation = null;

  @Data
  SimpleObject watchedPropDeepAnnotation = null;

  @Data
  String watchedDataImmediateAnnotation = "initialValue";

  @Data
  SimpleObject watchedDataPropertyImmediateAnnotation = new SimpleObject("initialValue");

  @Data
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
    return uncheckedCast(vue().$ref("watchPropTest"));
  }

  @JsMethod
  public SimpleObject getWatchedPropAnnotation() {
    return watchedPropAnnotation;
  }

  @JsMethod
  public SimpleObject getWatchedPropDeepAnnotation() {
    return watchedPropDeepAnnotation;
  }

  @JsMethod
  public SimpleObject getWatchedDataPropertyImmediateAnnotation() {
    return watchedDataPropertyImmediateAnnotation;
  }

  @JsMethod
  public SimpleObject getWatchedProp$WatchMethod() {
    return watchedProp$WatchMethod;
  }
}
