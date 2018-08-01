package com.axellience.vuegwtexamples.client.examples.recursive;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.annotations.component.PropDefault;
import com.axellience.vuegwt.core.client.component.IsVueComponent;

/**
 * @author Adrien Baron
 */
@Component(name = "recursive")
public class RecursiveComponent implements IsVueComponent {

  @Prop
  int counter;

  @PropDefault("counter")
  public int defaultCounter() {
    return 0;
  }
}
