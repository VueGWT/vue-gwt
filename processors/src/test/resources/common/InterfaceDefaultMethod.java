package common;

public interface InterfaceDefaultMethod {
  default String getDefaultText() {
    return "text";
  }
}