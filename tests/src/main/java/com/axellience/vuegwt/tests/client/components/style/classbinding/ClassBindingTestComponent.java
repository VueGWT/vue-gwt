package com.axellience.vuegwt.tests.client.components.style.classbinding;

import static com.axellience.vuegwt.core.client.tools.JsUtils.e;
import static com.axellience.vuegwt.core.client.tools.JsUtils.map;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsProperty;
import jsinterop.base.JsPropertyMap;

@Component
public class ClassBindingTestComponent implements IsVueComponent {

  @JsProperty
  boolean hasClassA = false;

  @JsProperty
  boolean hasClassB = false;

  @Computed
  public JsPropertyMap<Boolean> getComputedABClasses() {
    return map(e("class-a", hasClassA), e("class-b", hasClassB));
  }
}
