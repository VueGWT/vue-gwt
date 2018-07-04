package com.axellience.vuegwtexamples.client.examples.propdefaultvalue;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.annotations.component.PropDefault;
import com.axellience.vuegwt.core.client.component.IsVueComponent;

/**
 * @author Adrien Baron
 */
@Component
public class PropDefaultValueComponent implements IsVueComponent {

  @Prop
  String stringProp;

  @PropDefault("stringProp")
  String stringPropDefault() {
    return "Hello World";
  }
}
