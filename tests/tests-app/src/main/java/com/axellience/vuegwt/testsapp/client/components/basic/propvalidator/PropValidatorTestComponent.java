package com.axellience.vuegwt.testsapp.client.components.basic.propvalidator;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.annotations.component.PropValidator;
import com.axellience.vuegwt.core.client.component.IsVueComponent;

@Component
public class PropValidatorTestComponent implements IsVueComponent {

  @Prop
  int validatedProp;

  @PropValidator("validatedProp")
  boolean validateValidatedProp(int value) {
    return value < 100;
  }
}
