package com.axellience.vuegwt.core.client.component.options.data;

import com.axellience.vuegwt.core.client.component.options.NamedElementDefinition;

/**
 * Used to register the the data properties of the component based on the Java class attributes.
 *
 * @author Adrien Baron
 */
public class DataDefinition extends NamedElementDefinition {

  public DataDefinition(String javaName) {
    super(javaName);
  }
}
