package com.axellience.vuegwt.tests.client.components.basic.propdefault;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.annotations.component.PropDefault;
import com.axellience.vuegwt.core.client.component.IsVueComponent;

@Component
public class PropDefaultTestComponent implements IsVueComponent {

  @Prop
  String optionalProp;

  @PropDefault("optionalProp")
  String getDefaultValueOfOptionalProp() {
    return "default value";
  }
}
