package com.axellience.vuegwt.gwt2.template;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.generation.ComponentGenerationUtil;
import com.axellience.vuegwt.core.template.builder.TemplateImplBuilder;
import com.axellience.vuegwt.gwt2.template.compiler.GwtResourceFolder;
import com.axellience.vuegwt.core.template.parser.TemplateParser;
import com.axellience.vuegwt.core.template.parser.context.TemplateParserContext;
import com.axellience.vuegwt.core.template.parser.result.TemplateParserResult;
import com.coveo.nashorn_modules.Folder;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.resource.Resource;
import com.google.gwt.dev.resource.ResourceOracle;
import com.google.gwt.dev.util.Util;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import static com.axellience.vuegwt.core.client.template.ComponentTemplate.TEMPLATE_EXTENSION;
import static com.axellience.vuegwt.core.generation.GenerationNameUtil.COMPONENT_TEMPLATE_SUFFIX;
import static com.axellience.vuegwt.core.generation.GenerationNameUtil.componentJsTypeName;
import static com.axellience.vuegwt.core.generation.GenerationNameUtil.componentTemplateImplName;

/**
 * This generator parse and compile the HTML template.
 * The resulting object has information that can be passed to Vue when declaring the component.
 */
public final class TemplateGwtGenerator extends Generator
{
    @Override
    public String generate(TreeLogger logger, GeneratorContext generatorContext, String typeName)
    throws UnableToCompleteException
    {
        TypeOracle types = generatorContext.getTypeOracle();

        ClassName componentTypeName = ClassName.bestGuess(typeName.substring(0,
            typeName.length() - COMPONENT_TEMPLATE_SUFFIX.length()));

        JClassType componentJsType;
        try
        {
            componentJsType =
                types.getType(componentJsTypeName(componentTypeName).reflectionName());
        }
        catch (NotFoundException nfe)
        {
            logger.log(Type.ERROR, "Not Found", nfe);
            throw new UnableToCompleteException();
        }

        ClassName templateImplTypeName = componentTemplateImplName(componentTypeName);
        PrintWriter printWriter = generatorContext.tryCreate(logger,
            templateImplTypeName.packageName(),
            templateImplTypeName.simpleName());

        if (printWriter != null)
        {
            generateOnce(printWriter, logger, generatorContext, componentJsType, componentTypeName);
        }

        return templateImplTypeName.reflectionName();
    }

    private void generateOnce(PrintWriter printWriter, TreeLogger logger,
        GeneratorContext generatorContext, JClassType componentJsType, ClassName componentTypeName)
    throws UnableToCompleteException
    {
        String templateContent =
            getTemplateContent(generatorContext.getResourcesOracle(), logger, componentTypeName);

        TemplateParserContext templateParserContext = new TemplateParserContext(componentTypeName);
        registerFieldsAndMethodsInContext(templateParserContext, componentJsType);

        TemplateParserResult templateParserResult =
            new TemplateParser(logger).parseHtmlTemplate(templateContent, templateParserContext);

        createTemplateImpl(generatorContext,
            logger,
            printWriter,
            componentTypeName,
            templateParserResult);
    }

    /**
     * Create the template resource implementation based on the result of the template parser.
     * @param context The resource context (used to retrieve resources)
     * @param printWriter The source writer
     * @param componentTypeName The name of our Template class
     * @param templateParserResult The result of the HTML template parsed by {@link TemplateParser}
     * @throws UnableToCompleteException in case it fails to compile the HTML template to a JS
     * render function
     */
    private void createTemplateImpl(GeneratorContext context, TreeLogger logger,
        PrintWriter printWriter, ClassName componentTypeName,
        TemplateParserResult templateParserResult) throws UnableToCompleteException
    {
        // Resources are in the "client" folder to be included during GWT compilation
        Folder folder = new GwtResourceFolder(context.getResourcesOracle(),
            "com/axellience/vuegwt/gwt2/client/template/compiler");

        TemplateImplBuilder templateImplBuilder = new TemplateImplBuilder();
        TypeSpec templateImplType =
            templateImplBuilder.buildTemplateImpl(componentTypeName, templateParserResult, folder);

        // Write class
        JavaFile javaFile =
            JavaFile.builder(componentTypeName.packageName(), templateImplType).build();

        try
        {
            javaFile.writeTo(printWriter);
        }
        catch (IOException e)
        {
            logger.log(TreeLogger.ERROR,
                "Cannot write java file for component: " + componentTypeName.reflectionName());
            throw new UnableToCompleteException();
        }

        context.commit(logger, printWriter);
    }

    /**
     * Process the ComponentJsType class to register all the fields and methods visible in
     * the context.
     * @param templateResourceClass The class to process
     */
    private void registerFieldsAndMethodsInContext(TemplateParserContext templateParserContext,
        JClassType templateResourceClass)
    {
        // Stop recursion when getting to VueComponent class
        if (templateResourceClass == null || templateResourceClass
            .getQualifiedSourceName()
            .equals(VueComponent.class.getCanonicalName()))
            return;

        Arrays
            .stream(templateResourceClass.getFields())
            .filter(ComponentGenerationUtil::isFieldVisibleInJS)
            .forEach(jField -> templateParserContext.addRootVariable(jField
                .getType()
                .getQualifiedSourceName(), jField.getName()));

        Arrays
            .stream(templateResourceClass.getMethods())
            .filter(ComponentGenerationUtil::isMethodVisibleInTemplate)
            .map(JMethod::getName)
            .forEach(templateParserContext::addRootMethod);

        registerFieldsAndMethodsInContext(templateParserContext,
            templateResourceClass.getSuperclass());
    }

    private String getTemplateContent(ResourceOracle resourceOracle, TreeLogger logger,
        ClassName componentTypeName) throws UnableToCompleteException
    {
        String path = slashify(componentTypeName.reflectionName()) + TEMPLATE_EXTENSION;
        Resource resource = resourceOracle.getResource(path);

        // No resource for the template
        if (resource == null)
        {
            logger.log(TreeLogger.ERROR,
                "\nCouldn't find template for component \""
                    + componentTypeName.reflectionName()
                    + "\". If it doesn't have a template please set hasTemplate to false in the @Component annotation.");
            throw new UnableToCompleteException();
        }

        // Get template content from HTML file
        try
        {
            return Util.readStreamAsString(resource.openContents());
        }
        catch (IOException e)
        {
            logger.log(TreeLogger.ERROR,
                "\nFailed to open template file for component \""
                    + componentTypeName.reflectionName());
            throw new UnableToCompleteException();
        }
    }

    private static String slashify(String s)
    {
        return s.replace(".", "/").replace("$", ".");
    }
}
