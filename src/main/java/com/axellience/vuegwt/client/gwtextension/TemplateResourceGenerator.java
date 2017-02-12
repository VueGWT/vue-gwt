/*
 * Copyright 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.axellience.vuegwt.client.gwtextension;

import com.axellience.vuegwt.jsr69.TemplateProviderGenerator;
import com.axellience.vuegwt.jsr69.annotations.Computed;
import com.axellience.vuegwt.template.ExpressionInfo;
import com.axellience.vuegwt.template.TemplateParser;
import com.axellience.vuegwt.template.TemplateParserResult;
import com.axellience.vuegwt.template.VariableInfo;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.dev.util.Util;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.resources.ext.AbstractResourceGenerator;
import com.google.gwt.resources.ext.ResourceContext;
import com.google.gwt.resources.ext.ResourceGeneratorUtil;
import com.google.gwt.resources.ext.SupportsGeneratorResultCaching;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.user.rebind.StringSourceWriter;

import java.net.URL;
import java.util.Map.Entry;

/**
 * Source: GWT Project http://www.gwtproject.org/
 * <p>
 * Modified by Adrien Baron
 * Modification: Doesn't throw an exception if the resource doesn't exist, just return an empty
 * String
 */
public final class TemplateResourceGenerator extends AbstractResourceGenerator
    implements SupportsGeneratorResultCaching
{
    /**
     * Java compiler has a limit of 2^16 bytes for encoding string constants in a
     * class file. Since the max size of a character is 4 bytes, we'll limit the
     * number of characters to (2^14 - 1) to fit within one record.
     */
    private static final int MAX_STRING_CHUNK = 16383;

    @Override
    public String createAssignment(TreeLogger logger, ResourceContext context, JMethod method)
    throws UnableToCompleteException
    {
        URL[] resources;
        try
        {
            resources = ResourceGeneratorUtil.findResources(logger, context, method);
        }
        catch (UnableToCompleteException e)
        {
            resources = null;
        }

        URL resource = resources == null ? null : resources[0];

        SourceWriter sw = new StringSourceWriter();

        // No resource for the template
        if (resource == null)
        {
            sw.println("new " + TemplateResource.class.getName() + "() {");
            sw.indent();
            sw.println("public String getText() {return \"\";}");
            sw.println("public String getName() {return \"" + method.getName() + "\";}");
            sw.outdent();
            sw.println("}");
            return sw.toString();
        }

        if (!AbstractResourceGenerator.STRIP_COMMENTS)
        {
            // Convenience when examining the generated code.
            sw.println("// " + resource.toExternalForm());
        }

        TypeOracle typeOracle = context.getGeneratorContext().getTypeOracle();
        Source resourceAnnotation = method.getAnnotation(Source.class);
        String resourcePath = resourceAnnotation.value()[0];
        String typeName = resourcePath.substring(0, resourcePath.length() - 5).replaceAll("/", ".");

        // Start class
        sw.println("new " + typeName + TemplateProviderGenerator.TEMPLATE_RESOURCE_SUFFIX + "() {");
        sw.indent();

        // Get template content from HTML file
        String templateContent = Util.readURLAsString(resource);

        TemplateParser templateParser = new TemplateParser();
        TemplateParserResult templateParserResult =
            templateParser.parseHtmlTemplate(templateContent, typeOracle.findType(typeName));
        templateContent = templateParserResult.getTemplateWithReplacements();

        for (VariableInfo variableInfo : templateParserResult.getLocalVariables())
        {
            sw.println("@jsinterop.annotations.JsProperty");
            sw.println("public " + variableInfo.getType().getQualifiedSourceName() + " " +
                variableInfo.getJavaName() + ";");
        }

        // Add computed properties
        JClassType jClassType = typeOracle.findType(typeName);
        for (JMethod jMethod : jClassType.getMethods())
        {
            Computed computed = jMethod.getAnnotation(Computed.class);
            if (computed == null)
                continue;

            String propertyName = "$" + jMethod.getName();
            if (!"".equals(computed.propertyName()))
                propertyName = computed.propertyName();

            sw.println("@jsinterop.annotations.JsProperty");
            sw.println(jMethod.getReturnType().getQualifiedSourceName() + " " + propertyName + ";");
        }

        sw.println("public String getText() {");
        sw.indent();

        if (templateContent.length() > MAX_STRING_CHUNK)
        {
            writeLongString(sw, templateContent);
        }
        else
        {
            sw.println("return \"" + Generator.escape(templateContent) + "\";");
        }
        sw.outdent();
        sw.println("}");

        sw.println("public String getName() {");
        sw.indent();
        sw.println("return \"" + method.getName() + "\";");
        sw.outdent();
        sw.println("}");

        for (Entry<String, ExpressionInfo> entry : templateParserResult.getExpressions().entrySet())
        {
            sw.println("@jsinterop.annotations.JsMethod");
            sw.println("public String " + entry.getKey() + "() {");
            sw.indent();
            sw.println("return (" + entry.getValue().getExpression() + ") + \"\";");
            sw.outdent();
            sw.println("}");
        }

        for (Entry<String, String> entry : templateParserResult.getCollectionsExpressions()
            .entrySet())
        {
            sw.println("@jsinterop.annotations.JsMethod");
            sw.println("public Object " + entry.getKey() + "() {");
            sw.indent();
            sw.println("return " + entry.getValue() + ";");
            sw.outdent();
            sw.println("}");
        }

        sw.outdent();
        sw.println("}");

        return sw.toString();
    }

    public JType toPrimitiveType(JType type)
    {
        if ("java.lang.Boolean".equals(type.getQualifiedSourceName()))
            return JPrimitiveType.BOOLEAN;
        if ("java.lang.Byte".equals(type.getQualifiedSourceName()))
            return JPrimitiveType.BYTE;
        if ("java.lang.Character".equals(type.getQualifiedSourceName()))
            return JPrimitiveType.CHAR;
        if ("java.lang.Double".equals(type.getQualifiedSourceName()))
            return JPrimitiveType.DOUBLE;
        if ("java.lang.Float".equals(type.getQualifiedSourceName()))
            return JPrimitiveType.FLOAT;
        if ("java.lang.Integer".equals(type.getQualifiedSourceName()))
            return JPrimitiveType.INT;
        if ("java.lang.Long".equals(type.getQualifiedSourceName()))
            return JPrimitiveType.LONG;
        if ("java.lang.Short".equals(type.getQualifiedSourceName()))
            return JPrimitiveType.SHORT;

        return type;
    }

    /**
     * A single constant that is too long will crash the compiler with an out of
     * memory error. Break up the constant and generate code that appends using a
     * buffer.
     */
    private void writeLongString(SourceWriter sw, String toWrite)
    {
        sw.println("StringBuilder builder = new StringBuilder();");
        int offset = 0;
        int length = toWrite.length();
        while (offset < length - 1)
        {
            int subLength = Math.min(MAX_STRING_CHUNK, length - offset);
            sw.print("builder.append(\"");
            sw.print(Generator.escape(toWrite.substring(offset, offset + subLength)));
            sw.println("\");");
            offset += subLength;
        }
        sw.println("return builder.toString();");
    }
}
