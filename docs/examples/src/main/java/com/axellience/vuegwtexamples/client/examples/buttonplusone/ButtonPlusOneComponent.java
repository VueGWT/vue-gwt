package com.axellience.vuegwtexamples.client.examples.buttonplusone;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;

/**
 * @author Adrien Baron
 */
@Component
public class ButtonPlusOneComponent implements IsVueComponent {

  @Data
  int counter = 0;
}
