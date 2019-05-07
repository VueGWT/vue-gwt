package com.axellience.vuegwt.processors.component.template.parser;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic.Kind;

import com.axellience.vuegwt.processors.component.template.parser.context.StyleParserContext;

import net.htmlparser.jericho.Logger;

public class StyleParserLogger implements Logger {

  private final StyleParserContext context;
  private final Messager messager;

  StyleParserLogger(StyleParserContext context, Messager messager) {
    this.context = context;
    this.messager = messager;
  }

  void error(String message, String expression) {
    error(message + " In expression: " + expression);
  }

  @Override
  public void error(String message) {
    // Filter errors about invalid first attribute character
    // The @ sign is not recognized as valid when its valid in Vue GWT
    // A cleaner way would be to fork Jericho to add the support for @
    // But this filter should do for now.
    if (message.contains("contains attribute name with invalid first character")) {
      return;
    }

    printMessage(Kind.ERROR, message);
  }

  @Override
  public void warn(String message) {
    printMessage(Kind.WARNING, message);
  }

  @Override
  public void info(String message) {
    printMessage(Kind.NOTE, message);
  }

  @Override
  public void debug(String message) {
    printMessage(Kind.OTHER, message);
  }

  public void printMessage(Kind kind, String message) {
    messager.printMessage(kind,
        "In " + context.getTemplateName() + ": " + message,
        context.getComponentTypeElement());
  }

  @Override
  public boolean isErrorEnabled() {
    return true;
  }

  @Override
  public boolean isWarnEnabled() {
    return true;
  }

  @Override
  public boolean isInfoEnabled() {
    return true;
  }

  @Override
  public boolean isDebugEnabled() {
    return true;
  }
}
