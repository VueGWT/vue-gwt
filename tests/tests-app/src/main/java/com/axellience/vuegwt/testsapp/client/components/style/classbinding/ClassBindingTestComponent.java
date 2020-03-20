package com.axellience.vuegwt.testsapp.client.components.style.classbinding;

import static com.axellience.vuegwt.core.client.tools.JsUtils.e;
import static com.axellience.vuegwt.core.client.tools.JsUtils.map;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsMethod;
import jsinterop.base.JsPropertyMap;

@Component
public class ClassBindingTestComponent implements IsVueComponent {

  @Data
  boolean hasClassA = false;

  @Data
  boolean hasClassB = false;

  @Computed
  public JsPropertyMap<Boolean> getComputedABClasses() {
    return map(e("class-a", hasClassA), e("class-b", hasClassB));
  }

  @JsMethod
  public void setHasClassA(boolean hasClassA) {
    this.hasClassA = hasClassA;
  }

  @JsMethod
  public void setHasClassB(boolean hasClassB) {
    this.hasClassB = hasClassB;
  }
}
