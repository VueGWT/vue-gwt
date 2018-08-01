package com.axellience.vuegwtexamples.client.examples.kitten;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;

/**
 * @author Adrien Baron
 */
@Component
public class KittenComponent implements IsVueComponent {

  @Data
  KittenClientBundle myKittenBundle = KittenClientBundle.INSTANCE;
}