package com.axellience.vuegwt.tests.client.components.basic.watch;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Watch;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.tests.client.common.SimpleObject;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

@Component
public class WatchTestComponent implements IsVueComponent {

  @JsProperty
  SimpleObject watchedData = null;

  @JsProperty
  SimpleObject newValue;

  @JsProperty
  SimpleObject oldValue;

  @Watch("watchedData")
  public void onWatchedDataChange(SimpleObject newValue, SimpleObject oldValue) {
    this.newValue = newValue;
    this.oldValue = oldValue;
  }

  @JsMethod
  public void changeWatchedData(String property) {
    if (property == null) {
      watchedData = null;
    } else {
      watchedData = new SimpleObject();
      watchedData.setStringProperty(property);
    }
  }
}
