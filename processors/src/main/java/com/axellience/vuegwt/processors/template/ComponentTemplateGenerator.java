package com.axellience.vuegwt.processors.template;

import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwt.core.client.template.ComponentTemplate;
import com.axellience.vuegwt.core.generation.ComponentGenerationUtil;
import com.axellience.vuegwt.core.template.builder.TemplateBuilder;
import com.axellience.vuegwt.core.template.parser.TemplateParser;
import com.axellience.vuegwt.core.template.parser.context.TemplateParserContext;
import com.axellience.vuegwt.core.template.parser.context.localcomponents.LocalComponents;
import com.axellience.vuegwt.core.template.parser.result.TemplateParserResult;
import com.axellience.vuegwt.processors.component.ComponentJsTypeGenerator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import static com.axellience.vuegwt.core.client.template.ComponentTemplate.TEMPLATE_EXTENSION;
import static com.axellience.vuegwt.core.generation.ComponentGenerationUtil.getSuperComponentType;
import static com.axellience.vuegwt.core.generation.GenerationNameUtil.componentTemplateName;
import static com.axellience.vuegwt.core.generation.GenerationUtil.getComputedPropertyName;
import static com.axellience.vuegwt.core.generation.GenerationUtil.hasAnnotation;

/**
 * Generate a {@link ComponentTemplate} Impl class containing the compiled HTML template.
 * @author Adrien Baron
 */
public class ComponentTemplateGenerator
{
    private final Filer filer;
    private final Messager messager;

    public ComponentTemplateGenerator(ProcessingEnvironment processingEnvironment)
    {
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
    }

    public void generate(TypeElement componentTypeElement)
    {
        ClassName componentTypeName = ClassName.get(componentTypeElement);
        String templateContent = getTemplateContent(componentTypeName);

        // Initialize the template parser context based on the VueComponent type element
        TemplateParserContext templateParserContext =
            new TemplateParserContext(componentTypeName, new LocalComponents());
        registerFieldsAndMethodsInContext(templateParserContext,
            componentTypeElement,
            new HashSet<>(),
            new HashSet<>());

        // Parse the template
        TemplateParserResult templateParserResult =
            new TemplateParser().parseHtmlTemplate(templateContent, templateParserContext);

        // Build the TemplateImpl with the result
        TemplateBuilder templateBuilder = new TemplateBuilder();
        TypeSpec templateType = templateBuilder.buildTemplate(componentTypeName,
            templateParserResult);

        writeJavaFile(componentTypeElement, templateType);
    }

    /**
     * Process the ComponentJsType class to register all the fields and methods visible in
     * the context.
     * TODO: Improve this method by putting things together with {@link ComponentJsTypeGenerator}
     * @param componentTypeElement The class to process
     */
    private void registerFieldsAndMethodsInContext(TemplateParserContext templateParserContext,
        TypeElement componentTypeElement, Set<String> alreadyDoneVariable,
        Set<String> alreadyDoneMethods)
    {
        ElementFilter
            .fieldsIn(componentTypeElement.getEnclosedElements())
            .stream()
            .filter(ComponentGenerationUtil::isFieldVisibleInJS)
            .forEach(field -> {
                String name = field.getSimpleName().toString();
                if (alreadyDoneVariable.contains(name))
                    return;

                alreadyDoneVariable.add(name);
                templateParserContext.addRootVariable(ClassName.get(field.asType()), name);
            });

        ElementFilter
            .methodsIn(componentTypeElement.getEnclosedElements())
            .stream()
            .filter(method -> hasAnnotation(method, Computed.class))
            .filter(method -> !"void".equals(method.getReturnType().toString()))
            .forEach(method -> {
                String name = getComputedPropertyName(method);
                if (alreadyDoneVariable.contains(name))
                    return;
                alreadyDoneVariable.add(name);

                TypeMirror propertyType;
                if ("void".equals(method.getReturnType().toString()))
                    propertyType = method.getParameters().get(0).asType();
                else
                    propertyType = method.getReturnType();

                templateParserContext.addRootVariable(ClassName.get(propertyType), name);
            });

        ElementFilter
            .methodsIn(componentTypeElement.getEnclosedElements())
            .stream()
            .filter(ComponentGenerationUtil::isMethodVisibleInTemplate)
            .map(ExecutableElement::getSimpleName)
            .map(Object::toString)
            .forEach(methodName -> {
                if (alreadyDoneMethods.contains(methodName))
                    return;
                alreadyDoneMethods.add(methodName);

                templateParserContext.addRootMethod(methodName);
            });

        getSuperComponentType(componentTypeElement).ifPresent(superComponent -> registerFieldsAndMethodsInContext(
            templateParserContext,
            superComponent,
            alreadyDoneVariable,
            alreadyDoneMethods));
    }

    private void writeJavaFile(TypeElement componentTypeElement, TypeSpec templateImplType)
    {
        ClassName templateImpl = componentTemplateName(ClassName.get(componentTypeElement));

        try
        {
            JavaFile javaFile =
                JavaFile.builder(templateImpl.packageName(), templateImplType).build();

            JavaFileObject javaFileObject =
                filer.createSourceFile(templateImpl.reflectionName(), componentTypeElement);

            Writer writer = javaFileObject.openWriter();
            javaFile.writeTo(writer);
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private String getTemplateContent(ClassName componentTypeName)
    {
        String path = slashify(componentTypeName.reflectionName()) + TEMPLATE_EXTENSION;
        FileObject resource;
        try
        {
            resource = filer.getResource(StandardLocation.CLASS_OUTPUT, "", path);
        }
        catch (IOException e)
        {
            messager.printMessage(Kind.ERROR,
                "\nCouldn't find template for component \""
                    + componentTypeName.reflectionName()
                    + "\". If it doesn't have a template please set hasTemplate to false in the @Component annotation. Template File: "
                    + path);
            return null;
        }

        // Get template content from HTML file
        try
        {
            return resource.getCharContent(true).toString();
        }
        catch (IOException e)
        {
            messager.printMessage(Kind.ERROR,
                "\nFailed to open template file for component \""
                    + componentTypeName.reflectionName());
            return null;
        }
    }

    private static String slashify(String s)
    {
        return s.replace(".", "/").replace("$", ".");
    }
}
