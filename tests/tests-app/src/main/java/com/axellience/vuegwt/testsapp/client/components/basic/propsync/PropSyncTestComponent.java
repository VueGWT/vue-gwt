package com.axellience.vuegwt.testsapp.client.components.basic.propsync;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsMethod;

@Component
public class PropSyncTestComponent implements IsVueComponent {

  @Prop
  protected String parentProp;

  @JsMethod
  public void setParentPropToPressed() {
    vue().$emit("update:parentProp", "changedValue");
  }
}
