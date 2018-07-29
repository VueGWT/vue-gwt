package com.axellience.vuegwt.processors.component.template.parser.refs;

public class RefInfo {
  private String name;
  private boolean isArray;

  public RefInfo(String name, boolean isArray) {
    this.name = name;
    this.isArray = isArray;
  }

  public String getName() {
    return name;
  }

  public boolean isArray() {
    return isArray;
  }
}
