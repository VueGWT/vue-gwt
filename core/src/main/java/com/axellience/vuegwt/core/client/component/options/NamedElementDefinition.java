package com.axellience.vuegwt.core.client.component.options;

/**
 * Abstract class to link a Java and a Js name for a given property
 *
 * @author Adrien Baron
 */
public abstract class NamedElementDefinition {

  public final String javaName;
  public final String jsName;

  public NamedElementDefinition(String javaName) {
    this.javaName = javaName;
    this.jsName = javaName;
  }

  public NamedElementDefinition(String javaName, String jsName) {
    this.javaName = javaName;
    this.jsName = jsName;
  }
}
