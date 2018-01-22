package com.axellience.vuegwt.core.template.builder;

import com.axellience.vuegwt.core.client.template.ComponentTemplate;
import com.axellience.vuegwt.core.client.tools.VueGWTTools;
import com.axellience.vuegwt.core.template.compiler.VueTemplateCompiler;
import com.axellience.vuegwt.core.template.compiler.VueTemplateCompilerException;
import com.axellience.vuegwt.core.template.compiler.VueTemplateCompilerResult;
import com.axellience.vuegwt.core.template.parser.TemplateParser;
import com.axellience.vuegwt.core.template.parser.result.TemplateExpression;
import com.axellience.vuegwt.core.template.parser.result.TemplateParserResult;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import jsinterop.annotations.JsMethod;
import jsinterop.base.Js;

import javax.lang.model.element.Modifier;

import static com.axellience.vuegwt.core.generation.GenerationNameUtil.componentJsTypeName;
import static com.axellience.vuegwt.core.generation.GenerationNameUtil.componentTemplateName;
import static com.axellience.vuegwt.core.generation.GenerationUtil.getUnusableByJSAnnotation;

public class TemplateBuilder
{
    /**
     * Create the template resource implementation based on the result of the template parser.
     * @param componentTypeName The name of our Template class
     * @param templateParserResult The result of the HTML template parsed by {@link TemplateParser}
     * render function
     * @return The built Java class representing our template
     */
    public TypeSpec buildTemplate(ClassName componentTypeName,
        TemplateParserResult templateParserResult)
    {
        Builder templateImplBuilder = TypeSpec
            .classBuilder(componentTemplateName(componentTypeName))
            .addModifiers(Modifier.PUBLIC)
            .superclass(componentJsTypeName(componentTypeName))
            .addSuperinterface(ParameterizedTypeName.get(ClassName.get(ComponentTemplate.class),
                componentTypeName));

        // Compile the resulting HTML template String
        compileTemplateString(templateImplBuilder,
            templateParserResult.getProcessedTemplate());

        // Process the java expressions from the template
        processTemplateExpressions(templateImplBuilder, templateParserResult);

        return templateImplBuilder.build();
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
            VueTemplateCompiler vueTemplateCompiler =
                new VueTemplateCompiler();
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
            .addModifiers(Modifier.PUBLIC)
            .returns(String.class)
            .addStatement("return $S", result.getRenderFunction());

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
            {
                staticFunctions.add(", ");
            }
            else
            {
                isFirst = false;
            }
            staticFunctions.add("$S", staticRenderFunction);
        }

        MethodSpec.Builder getStaticRenderFunctionsBuilder = MethodSpec
            .methodBuilder("getStaticRenderFunctions")
            .addModifiers(Modifier.PUBLIC)
            .returns(String[].class)
            .addStatement("return new String[] { $L }", staticFunctions.build());

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
            generateTemplateExpressionMethod(templateBuilder, expression);
        }

        generateGetTemplateMethods(templateBuilder, templateParserResult);
    }

    /**
     * Generate the Java method for an expression in the Template
     * @param templateBuilder The template builder
     * @param expression An expression from the HTML template
     */
    private void generateTemplateExpressionMethod(Builder templateBuilder,
        TemplateExpression expression)
    {
        MethodSpec.Builder templateExpressionMethodBuilder = MethodSpec
            .methodBuilder(expression.getId())
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(JsMethod.class)
            .addAnnotation(getUnusableByJSAnnotation())
            .returns(expression.getType());

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

    /**
     * Generate the method to get the list of methods from the template
     * @param templateBuilder The template builder
     * @param templateParserResult Result from the parsing of the HTML Template
     */
    private void generateGetTemplateMethods(Builder templateBuilder,
        TemplateParserResult templateParserResult)
    {
        MethodSpec.Builder getStaticRenderFunctionsBuilder = MethodSpec
            .methodBuilder("getTemplateMethodsCount")
            .addModifiers(Modifier.PUBLIC)
            .returns(TypeName.INT)
            .addStatement("return $L", templateParserResult.getExpressions().size());

        templateBuilder.addMethod(getStaticRenderFunctionsBuilder.build());
    }
}
