package com.axellience.vuegwt.processors.component.template.builder;

import static com.axellience.vuegwt.processors.utils.GeneratorsNameUtil.vModelFieldToPlaceHolderField;
import static com.axellience.vuegwt.processors.utils.GeneratorsUtil.getFieldMarkingValueForType;
import static com.axellience.vuegwt.processors.utils.GeneratorsUtil.getUnusableByJSAnnotation;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.tools.VueGWTTools;
import com.axellience.vuegwt.processors.component.ComponentExposedTypeGenerator;
import com.axellience.vuegwt.processors.component.template.builder.compiler.VueTemplateCompiler;
import com.axellience.vuegwt.processors.component.template.builder.compiler.VueTemplateCompilerException;
import com.axellience.vuegwt.processors.component.template.builder.compiler.VueTemplateCompilerResult;
import com.axellience.vuegwt.processors.component.template.parser.TemplateParser;
import com.axellience.vuegwt.processors.component.template.parser.result.TemplateExpression;
import com.axellience.vuegwt.processors.component.template.parser.result.TemplateParserResult;
import com.axellience.vuegwt.processors.component.template.parser.variable.VariableInfo;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import elemental2.core.Function;
import java.util.stream.Collectors;
import javax.lang.model.element.Modifier;
import jsinterop.annotations.JsMethod;
import jsinterop.base.Js;

public class TemplateMethodsBuilder {

  /**
   * Add Template methods to @{@link IsVueComponent} ExposedType based on the result of the template
   * parser.
   *
   * @param exposedTypeGenerator Class generating the ExposedType
   * @param templateParserResult The result of the HTML template parsed by {@link TemplateParser}
   */
  public void addTemplateMethodsToComponentExposedType(
      ComponentExposedTypeGenerator exposedTypeGenerator,
      TemplateParserResult templateParserResult) {
    // Compile the resulting HTML template String
    compileTemplateString(exposedTypeGenerator, templateParserResult);

    // Process the java expressions from the template
    processTemplateExpressions(exposedTypeGenerator, templateParserResult);
  }

  /**
   * Compile the HTML template and transform it to a JS render function.
   *
   * @param exposedTypeGenerator Class generating the ExposedType
   * @param templateParserResult The result of the HTML template parsed by {@link TemplateParser}
   */
  private void compileTemplateString(ComponentExposedTypeGenerator exposedTypeGenerator,
      TemplateParserResult templateParserResult) {
    VueTemplateCompilerResult compilerResult;
    try {
      VueTemplateCompiler vueTemplateCompiler = new VueTemplateCompiler();
      compilerResult = vueTemplateCompiler.compile(templateParserResult.getProcessedTemplate());
    } catch (VueTemplateCompilerException e) {
      e.printStackTrace();
      throw new RuntimeException();
    }

    generateGetRenderFunction(exposedTypeGenerator, compilerResult, templateParserResult);
    generateGetStaticRenderFunctions(exposedTypeGenerator, compilerResult);
  }

  /**
   * Generate the method that returns the body of the render function.
   *
   * @param exposedTypeGenerator Class generating the ExposedType
   * @param result The result from compilation using vue-template-compiler
   * @param templateParserResult The result of the HTML template parsed by {@link TemplateParser}
   */
  private void generateGetRenderFunction(ComponentExposedTypeGenerator exposedTypeGenerator,
      VueTemplateCompilerResult result,
      TemplateParserResult templateParserResult) {
    MethodSpec.Builder getRenderFunctionBuilder = MethodSpec
        .methodBuilder("getRenderFunction")
        .addModifiers(Modifier.PRIVATE)
        .returns(Function.class)
        .addStatement("String renderFunctionString = $S", result.getRenderFunction());

    for (VariableInfo vModelField : templateParserResult.getvModelDataFields()) {
      String placeHolderVModelValue = vModelFieldToPlaceHolderField(vModelField.getName());
      getRenderFunctionBuilder
          .addStatement(
              "renderFunctionString = $T.replaceVariableInRenderFunction(renderFunctionString, $S, this, () -> this.$L = $L)",
              VueGWTTools.class,
              placeHolderVModelValue,
              vModelField.getName(),
              getFieldMarkingValueForType(vModelField.getType())
          );
    }

    getRenderFunctionBuilder
        .addStatement("return new $T(renderFunctionString)", Function.class);

    exposedTypeGenerator.getClassBuilder().addMethod(getRenderFunctionBuilder.build());
  }

  /**
   * Generate the method that returns the body of the static render functions.
   *
   * @param exposedTypeGenerator Class generating the ExposedType
   * @param result The result from compilation using vue-template-compiler
   */
  private void generateGetStaticRenderFunctions(ComponentExposedTypeGenerator exposedTypeGenerator,
      VueTemplateCompilerResult result) {
    CodeBlock.Builder staticFunctions = CodeBlock.builder();

    boolean isFirst = true;
    for (String staticRenderFunction : result.getStaticRenderFunctions()) {
      if (!isFirst) {
        staticFunctions.add(", ");
      } else {
        isFirst = false;
      }

      staticFunctions.add("new $T($S)", Function.class, staticRenderFunction);
    }

    MethodSpec.Builder getStaticRenderFunctionsBuilder = MethodSpec
        .methodBuilder("getStaticRenderFunctions")
        .addModifiers(Modifier.PRIVATE)
        .returns(Function[].class)
        .addStatement("return new $T[] { $L }", Function.class, staticFunctions.build());

    exposedTypeGenerator.getClassBuilder().addMethod(getStaticRenderFunctionsBuilder.build());
  }

  /**
   * Process the expressions found in the HTML template
   *
   * @param exposedTypeGenerator Class generating the ExposedType
   * @param templateParserResult Result from the parsing of the HTML Template
   */
  private void processTemplateExpressions(ComponentExposedTypeGenerator exposedTypeGenerator,
      TemplateParserResult templateParserResult) {
    for (TemplateExpression expression : templateParserResult.getExpressions()) {
      generateTemplateExpressionMethod(exposedTypeGenerator,
          expression,
          templateParserResult.getTemplateName());
    }

    // Declare methods in the component
    String templateMethods = templateParserResult.getExpressions()
        .stream()
        .map(expression -> "p." + expression.getId())
        .collect(Collectors.joining(", "));

    exposedTypeGenerator.getOptionsBuilder()
        .addStatement("options.registerTemplateMethods($L)", templateMethods);
  }

  /**
   * Generate the Java method for an expression in the Template
   *
   * @param exposedTypeGenerator Class generating the ExposedType
   * @param templateName The name of the Template the expression is from
   */
  private void generateTemplateExpressionMethod(ComponentExposedTypeGenerator exposedTypeGenerator,
      TemplateExpression expression,
      String templateName) {
    MethodSpec.Builder templateExpressionMethodBuilder = MethodSpec
        .methodBuilder(expression.getId())
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(JsMethod.class)
        .addAnnotation(getUnusableByJSAnnotation())
        .returns(expression.getType());

    String expressionPosition = templateName;
    if (expression.getLineInHtml() != null) {
      expressionPosition += ", line " + expression.getLineInHtml();
    }

    templateExpressionMethodBuilder.addComment(expressionPosition);

    expression
        .getParameters()
        .forEach(parameter -> templateExpressionMethodBuilder.addParameter(parameter.getType(),
            parameter.getName()));

    if (expression.isReturnString()) {
      templateExpressionMethodBuilder.addStatement("return $T.templateExpressionToString($L)",
          VueGWTTools.class,
          expression.getBody());
    } else if (expression.isReturnVoid()) {
      templateExpressionMethodBuilder.addStatement("$L", expression.getBody());
    } else if (expression.isReturnAny()) {
      templateExpressionMethodBuilder.addStatement("return $T.asAny($L)",
          Js.class,
          expression.getBody());
    } else if (expression.isShouldCast()) {
      templateExpressionMethodBuilder.addStatement("return ($T) ($L)",
          expression.getType(),
          expression.getBody());
    } else {
      templateExpressionMethodBuilder.addStatement("return $L", expression.getBody());
    }

    exposedTypeGenerator.getClassBuilder().addMethod(templateExpressionMethodBuilder.build());
    exposedTypeGenerator.addMethodToProto(expression.getId());
  }
}
