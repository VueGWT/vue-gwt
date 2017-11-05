package com.axellience.vuegwt.gwt2.template;

import com.axellience.vuegwt.gwt2.template.compiler.GwtResourceFolder;
import com.axellience.vuegwt.gwt2.template.builder.TemplateImplBuilder;
import com.axellience.vuegwt.gwt2.template.parser.TemplateParser;
import com.axellience.vuegwt.gwt2.template.parser.result.TemplateParserResult;
import com.coveo.nashorn_modules.Folder;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
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
    public String generate(TreeLogger logger, GeneratorContext context, String typeName)
    throws UnableToCompleteException
    {
        TypeOracle types = context.getTypeOracle();

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
        PrintWriter printWriter = context.tryCreate(logger,
            templateImplTypeName.packageName(),
            templateImplTypeName.simpleName());

        if (printWriter != null)
        {
            generateOnce(printWriter, logger, context, componentJsType, componentTypeName);
        }

        return templateImplTypeName.reflectionName();
    }

    private void generateOnce(PrintWriter printWriter, TreeLogger logger, GeneratorContext context,
        JClassType componentJsType, ClassName componentTypeName) throws UnableToCompleteException
    {
        Resource resource = getResource(componentTypeName, context.getResourcesOracle());
        // No resource for the template
        if (resource == null)
        {
            logger.log(TreeLogger.ERROR,
                "\nCouldn't find template for component \""
                    + componentJsType.getQualifiedSourceName()
                    + "\". If it doesn't have a template please set hasTemplate to false in the @Component annotation.");
            return;
        }

        // Get template content from HTML file
        String templateContent;
        try
        {
            templateContent = Util.readStreamAsString(resource.openContents());
        }
        catch (IOException e)
        {
            logger.log(TreeLogger.ERROR,
                "\nFailed to open template file for component \""
                    + componentJsType.getQualifiedSourceName());
            return;
        }

        // Process it
        TemplateParserResult templateParserResult =
            new TemplateParser().parseHtmlTemplate(templateContent, componentJsType);

        createTemplateImpl(context, logger, printWriter, componentTypeName, templateParserResult);
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
        TypeSpec templateImplType = templateImplBuilder.buildTemplateImpl(componentTypeName,
            templateParserResult,
            folder);

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

    private Resource getResource(ClassName componentTypeName, ResourceOracle resourceOracle)
    {
        String path = slashify(componentTypeName.reflectionName()) + TEMPLATE_EXTENSION;
        return resourceOracle.getResource(path);
    }

    private static String slashify(String s)
    {
        return s.replace(".", "/").replace("$", ".");
    }
}
