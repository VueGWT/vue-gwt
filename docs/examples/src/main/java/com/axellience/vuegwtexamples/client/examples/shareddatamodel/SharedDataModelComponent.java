package com.axellience.vuegwtexamples.client.examples.shareddatamodel;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;

/**
 * @author Adrien Baron
 */
@Component(useFactory = false)
public class SharedDataModelComponent implements IsVueComponent {

  @Data
  int counter = 0;
}
