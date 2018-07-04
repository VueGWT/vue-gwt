package com.axellience.vuegwtexamples.client.examples.canhide;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;

@Component
public class CanHideComponent implements IsVueComponent {

  @Data
  boolean visible = true;
}