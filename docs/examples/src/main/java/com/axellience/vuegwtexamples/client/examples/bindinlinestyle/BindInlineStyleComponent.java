package com.axellience.vuegwtexamples.client.examples.bindinlinestyle;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;

/**
 * @author Adrien Baron
 */
@Component
public class BindInlineStyleComponent implements IsVueComponent {

  @Data
  String activeColor = "red";
  @Data
  float fontSize = 20;
}
