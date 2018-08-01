package com.axellience.vuegwt.tests.client.components.jsinterop;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;

@Component
public class ComputedOverrideComponent implements IsVueComponent {
  @Data
  String data = "DATA";

  @Computed
  public String a() {
    return "COMPUTED";
  }
}
