package com.axellience.vuegwtexamples.client.examples.shareddatamodel;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component(useFactory = false)
public class SharedDataModelComponent implements IsVueComponent {

  @JsProperty
  int counter = 0;
}
