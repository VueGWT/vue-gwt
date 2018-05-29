package com.axellience.vuegwt.processors.component.template.builder;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.tools.VueGWTTools;
import com.axellience.vuegwt.processors.component.template.builder.compiler.VueTemplateCompiler;
import com.axellience.vuegwt.processors.component.template.builder.compiler.VueTemplateCompilerException;
import com.axellience.vuegwt.processors.component.template.builder.compiler.VueTemplateCompilerResult;
import com.axellience.vuegwt.processors.component.template.parser.TemplateParser;
import com.axellience.vuegwt.processors.component.template.parser.result.TemplateExpression;
import com.axellience.vuegwt.processors.component.template.parser.result.TemplateParserResult;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import elemental2.core.Function;
import jsinterop.annotations.JsMethod;
import jsinterop.base.Js;

import javax.lang.model.element.Modifier;

import static com.axellience.vuegwt.processors.utils.GeneratorsUtil.getUnusableByJSAnnotation;

public class TemplateMethodsBuilder
{
    /**
     * Add Template methods to @{@link IsVueComponent} ExposedType based on the result of the template parser.
     * @param componentExposedTypeBuilder Builder for the ExposedType class
     * @param templateParserResult The result of the HTML template parsed by {@link TemplateParser}
     * render function
     */
    public void addTemplateMethodsToComponentExposedType(Builder componentExposedTypeBuilder,
        TemplateParserResult templateParserResult)
    {
        // Compile the resulting HTML template String
        compileTemplateString(componentExposedTypeBuilder, templateParserResult.getProcessedTemplate());

        // Process the java expressions from the template
        processTemplateExpressions(componentExposedTypeBuilder, templateParserResult);
    }

    /**
     * Compile the HTML template and transform it to a JS render function.
     * @param templateBuilder The template builder
     * @param templateString The HTML template string to compile
     */
    private void compileTemplateString(Builder templateBuilder, String templateString)
    {
        VueTemplateCompilerResult result;
        try
        {
            VueTemplateCompiler vueTemplateCompiler = new VueTemplateCompiler();
            result = vueTemplateCompiler.compile(templateString);
        }
        catch (VueTemplateCompilerException e)
        {
            e.printStackTrace();
            throw new RuntimeException();
        }

        generateGetRenderFunction(templateBuilder, result);
        generateGetStaticRenderFunctions(templateBuilder, result);
    }

    /**
     * Generate the method that returns the body of the render function.
     * @param templateBuilder The template builder
     * @param result The result from compilation using vue-template-compiler
     */
    private void generateGetRenderFunction(Builder templateBuilder,
        VueTemplateCompilerResult result)
    {
        MethodSpec.Builder getRenderFunctionBuilder = MethodSpec
            .methodBuilder("getRenderFunction")
            .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
            .returns(Function.class)
            .addStatement("return new $T($S)", Function.class, result.getRenderFunction());

        templateBuilder.addMethod(getRenderFunctionBuilder.build());
    }

    /**
     * Generate the method that returns the body of the static render functions.
     * @param templateBuilder The template builder
     * @param result The result from compilation using vue-template-compiler
     */
    private void generateGetStaticRenderFunctions(Builder templateBuilder,
        VueTemplateCompilerResult result)
    {
        CodeBlock.Builder staticFunctions = CodeBlock.builder();

        boolean isFirst = true;
        for (String staticRenderFunction : result.getStaticRenderFunctions())
        {
            if (!isFirst)
                staticFunctions.add(", ");
            else
                isFirst = false;

            staticFunctions.add("new $T($S)", Function.class, staticRenderFunction);
        }

        MethodSpec.Builder getStaticRenderFunctionsBuilder = MethodSpec
            .methodBuilder("getStaticRenderFunctions")
            .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
            .returns(Function[].class)
            .addStatement("return new $T[] { $L }", Function.class, staticFunctions.build());

        templateBuilder.addMethod(getStaticRenderFunctionsBuilder.build());
    }

    /**
     * Process the expressions found in the HTML template
     * @param templateBuilder The template builder
     * @param templateParserResult Result from the parsing of the HTML Template
     */
    private void processTemplateExpressions(Builder templateBuilder,
        TemplateParserResult templateParserResult)
    {
        for (TemplateExpression expression : templateParserResult.getExpressions())
        {
            generateTemplateExpressionMethod(templateBuilder,
                expression,
                templateParserResult.getTemplateName());
        }
    }

    /**
     * Generate the Java method for an expression in the Template
     * @param templateBuilder The template builder
     * @param expression An expression from the HTML template
     * @param templateName The name of the Template the expression is from
     */
    private void generateTemplateExpressionMethod(Builder templateBuilder,
        TemplateExpression expression, String templateName)
    {
        MethodSpec.Builder templateExpressionMethodBuilder = MethodSpec
            .methodBuilder(expression.getId())
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(JsMethod.class)
            .addAnnotation(getUnusableByJSAnnotation())
            .returns(expression.getType());

        String expressionPosition = templateName;
        if (expression.getLineInHtml() != null)
            expressionPosition += ", line " + expression.getLineInHtml();

        templateExpressionMethodBuilder.addComment(expressionPosition);

        expression
            .getParameters()
            .forEach(parameter -> templateExpressionMethodBuilder.addParameter(parameter.getType(),
                parameter.getName()));

        if (expression.isReturnString())
        {
            templateExpressionMethodBuilder.addStatement("return $T.templateExpressionToString($L)",
                VueGWTTools.class,
                expression.getBody());
        }
        else if (expression.isReturnVoid())
        {
            templateExpressionMethodBuilder.addStatement("$L", expression.getBody());
        }
        else if (expression.isReturnAny())
        {
            templateExpressionMethodBuilder.addStatement("return $T.asAny($L)",
                Js.class,
                expression.getBody());
        }
        else if (expression.isShouldCast())
        {
            templateExpressionMethodBuilder.addStatement("return ($T) ($L)",
                expression.getType(),
                expression.getBody());
        }
        else
        {
            templateExpressionMethodBuilder.addStatement("return $L", expression.getBody());
        }

        templateBuilder.addMethod(templateExpressionMethodBuilder.build());
    }
}
