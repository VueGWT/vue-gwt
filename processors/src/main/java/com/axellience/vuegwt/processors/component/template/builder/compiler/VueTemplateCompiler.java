package com.axellience.vuegwt.processors.component.template.builder.compiler;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * Compile an HTML Vue template to JS render function using Nashorn and the vue-template-compiler.
 *
 * @author Adrien Baron
 */
public class VueTemplateCompiler {

  private static NashornScriptEngine engine;

  public VueTemplateCompiler() {
    // Engine is cached between instance to avoid creating at each compilation
    if (engine == null) {
      initEngine();
    }
  }

  /**
   * Init the Nashorn engine and load the Vue compiler in it.
   */
  private void initEngine() {
    engine = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");

    try {
      engine.eval("(function(global){global.global = global})(this);");
      engine.eval(NashornVueTemplateCompiler.NASHORN_VUE_TEMPLATE_COMPILER);
    } catch (ScriptException e) {
      e.printStackTrace();
    }
  }

  /**
   * Compile the given HTML template to JS functions using vue-template-compiler.
   *
   * @param htmlTemplate The HTML Component template to compile
   * @return An object containing the render functions
   * @throws VueTemplateCompilerException If the compilation fails
   */
  public VueTemplateCompilerResult compile(String htmlTemplate)
      throws VueTemplateCompilerException {
    ScriptObjectMirror templateCompilerResult;
    try {
      templateCompilerResult =
          (ScriptObjectMirror) engine.invokeFunction("compile", htmlTemplate);
    } catch (ScriptException | NoSuchMethodException e) {
      e.printStackTrace();
      throw new VueTemplateCompilerException(
          "An error occurred while compiling the template: "
              + htmlTemplate
              + " -> "
              + e.getMessage());
    }

    String renderFunction = (String) templateCompilerResult.get("render");
    String[] staticRenderFunctions =
        ((ScriptObjectMirror) templateCompilerResult.get("staticRenderFns")).to(String[].class);

    return new VueTemplateCompilerResult(renderFunction, staticRenderFunctions);
  }
}
