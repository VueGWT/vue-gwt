package com.axellience.vuegwt.processors.component.template.parser.context;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.TypeElement;

import com.axellience.vuegwt.core.client.component.IsVueComponent;

public class StyleParserContext {
  private final TypeElement componentTypeElement;

  /**
   * In some cases mandatory attributes must be added to each element during template parsing, for example to support scoped styles
   */
  private final Map<String, String> mandatoryAttributes = new HashMap<>();

  /**
   * Build the context based on a given {@link IsVueComponent} Class.
   *
   * @param componentTypeElement
   *          The {@link IsVueComponent} class we process in this context
   */
  public StyleParserContext(final TypeElement componentTypeElement) {
    this.componentTypeElement = componentTypeElement;
  }

  /**
   * Simple getter for the currently processed {@link IsVueComponent} Template name. Used for
   * debugging.
   *
   * @return The currently process {@link IsVueComponent} Template name
   */
  public String getTemplateName() {
    return componentTypeElement.getSimpleName().toString() + ".scss";
  }

  public TypeElement getComponentTypeElement() {
    return componentTypeElement;
  }

  public Map<String, String> getMandatoryAttributes() {
    return mandatoryAttributes;
  }
}
