package com.axellience.vuegwt.testsapp.client.components.basic.propdefault;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;

@Component(components = PropDefaultTestComponent.class)
public class PropDefaultParentTestComponent implements IsVueComponent {

  @Data
  String propParent = "parent value";
}
