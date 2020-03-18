package com.axellience.vuegwt.processors.component.template.builder.compiler;

import java.util.List;
import org.mozilla.javascript.ConsString;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

/**
 * Compile an HTML Vue template to JS render function using Rhino and the vue-template-compiler.
 *
 * @author Adrien Baron
 */
public class VueTemplateCompiler {

  private static Context context;
  private static Scriptable scope;

  public VueTemplateCompiler() {
    // Engine is cached between instance to avoid creating at each compilation
    if (context == null) {
      initEngine();
    }
  }

  /**
   * Init the Rhino engine and load the Vue compiler in it.
   */
  private void initEngine() {
    context = Context.enter();
    scope = context.initStandardObjects();

    context.evaluateString(scope, "(function(global){global.global = global})(this);", "<cmd>", 1,
        null);
    context
        .evaluateString(scope, JsVueTemplateCompiler.JS_VUE_TEMPLATE_COMPILER, "<cmd>",
            1, null);
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

    NativeObject templateCompilerResult;
    Object vueCompilerFunction = scope.get("compile", scope);
    if (!(vueCompilerFunction instanceof Function)) {
      throw new VueTemplateCompilerException(
          "An error occurred while compiling the template: " + htmlTemplate);
    }

    Object[] vueCompilerArgs = {htmlTemplate};
    Function f = (Function) vueCompilerFunction;
    Object result = f.call(context, scope, scope, vueCompilerArgs);
    templateCompilerResult = (NativeObject) Context.jsToJava(result, NativeObject.class);
    String render = ((ConsString) templateCompilerResult.get("render")).toString();

    String[] staticRenderFunctions = ((List<?>) templateCompilerResult
        .get("staticRenderFns")).stream().map(s -> ((ConsString) s).toString())
        .toArray(String[]::new);

    return new VueTemplateCompilerResult(render, staticRenderFunctions);
  }
}
