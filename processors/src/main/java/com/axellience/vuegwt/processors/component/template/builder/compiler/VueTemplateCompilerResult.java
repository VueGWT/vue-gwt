package com.axellience.vuegwt.processors.component.template.builder.compiler;

/**
 * @author Adrien Baron
 */
public class VueTemplateCompilerResult {

  private String renderFunction;
  private String[] staticRenderFunctions;

  public VueTemplateCompilerResult(String renderFunction, String[] staticRenderFunctions) {
    this.renderFunction = renderFunction;
    this.staticRenderFunctions = staticRenderFunctions;
  }

  public String getRenderFunction() {
    return renderFunction;
  }

  public String[] getStaticRenderFunctions() {
    return staticRenderFunctions;
  }
}
