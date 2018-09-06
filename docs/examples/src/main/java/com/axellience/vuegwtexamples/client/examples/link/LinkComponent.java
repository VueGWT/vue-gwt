package com.axellience.vuegwtexamples.client.examples.link;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;

@Component
public class LinkComponent implements IsVueComponent {

  @Data
  String linkName = "Hello Vue GWT!";
  @Data
  String linkTarget = "https://github.com/VueGWT/vue-gwt";
}