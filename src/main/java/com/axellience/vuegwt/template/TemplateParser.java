package com.axellience.vuegwt.template;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.google.gwt.core.ext.typeinfo.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.axellience.vuegwt.client.gwtextension.TemplateResource.COLLECTION_ARRAY_SUFFIX;

/**
 * @author Adrien Baron
 */
public class TemplateParser
{
    private static Pattern VUE_ATTR_PATTERN = Pattern.compile("^(v-|:|@).*");
    private static Pattern VUE_MUSTACHE_PATTERN = Pattern.compile("\\{\\{.*?}}");

    public TemplateParserResult parseHtmlTemplate(String htmlTemplate, JClassType vueComponentClass)
    {
        TemplateParserResult result = new TemplateParserResult();
        Parser parser = Parser.htmlParser();
        parser.settings(new ParseSettings(true, true)); // tag, attribute preserve case
        Document doc = parser.parseInput(htmlTemplate, "");

        TemplateParserContext parserContext = new TemplateParserContext(vueComponentClass);
        processElement(doc, parserContext, result);

        result.setTemplateWithReplacements(doc.body().html());
        return result;
    }

    private void processElement(Node node, TemplateParserContext context,
        TemplateParserResult result)
    {
        // Deal with mustache in the Nodes text
        boolean shouldPopContext = false;
        if (node instanceof TextNode)
        {
            String elementText = ((TextNode) node).text();

            Matcher matcher = VUE_MUSTACHE_PATTERN.matcher(elementText);

            int lastEnd = 0;
            StringBuilder newText = new StringBuilder();
            while (matcher.find())
            {
                int start = matcher.start();
                int end = matcher.end();
                if (start > 0)
                    newText.append(elementText.substring(lastEnd, start));

                String expressionString = elementText.substring(start + 2, end - 2).trim();
                if (context.isInContext())
                {
                    Expression expression = JavaParser.parseExpression(expressionString);
                    renameLocalVariables(expression, context);
                    expressionString = expression.toString();
                }

                String expressionId = addExpressionToTemplate(expressionString, result);
                newText.append("{{ ").append(expressionId).append(" }}");
                lastEnd = end;
            }
            if (lastEnd > 0)
            {
                newText.append(elementText.substring(lastEnd));
                ((TextNode) node).text(newText.toString());
            }
        }
        else if (node instanceof Element)
        {
            // First try to find v-for
            for (Attribute attribute : node.attributes())
            {
                if (!"v-for".equals(attribute.getKey()))
                    continue;

                // Add a context
                context.addContext();
                shouldPopContext = true;

                // Process the for expression and declare local variables if necessary
                attribute.setValue(processVForExpression(attribute.getValue(), context, result));
            }

            // Iterate on element attributes
            for (Attribute attribute : node.attributes())
            {
                if ("v-for".equals(attribute.getKey()))
                    continue;

                if (!VUE_ATTR_PATTERN.matcher(attribute.getKey()).matches())
                    continue;

                if (":class".equals(attribute.getKey()) ||
                    "v-bind:class".equals(attribute.getKey()))
                    continue;

                String expressionString = attribute.getValue();
                if (context.isInContext())
                {
                    Expression expression = JavaParser.parseExpression(expressionString);
                    renameLocalVariables(expression, context);
                    expressionString = expression.toString();
                }

                attribute.setValue(addExpressionToTemplate(expressionString, result));
            }
        }

        // Recurse downwards
        node.childNodes().
            forEach(child -> processElement(child, context, result));

        // After recursing downward, pop the context
        if (shouldPopContext)
        {
            ContextLayer contextLayer = context.popContext();
            result.addLocalVariables(contextLayer.getVariables());
        }
    }

    private String processVForExpression(String vForValue, TemplateParserContext context,
        TemplateParserResult result)
    {
        String[] splitValue = vForValue.split(" in ");
        if (splitValue.length != 2)
            throw new InvalidExpressionException("Invalid v-for found: " + vForValue);

        String loopVariableName = splitValue[0];

        Expression inExpression = JavaParser.parseExpression(splitValue[1]);
        renameLocalVariables(inExpression, context);

        JParameterizedType inExpressionType =
            getVForExpressionType(inExpression, context).isParameterized();

        if (inExpressionType == null || inExpressionType.getTypeArgs().length != 1)
            throw new InvalidExpressionException("Couldn't determine v-for type: " + vForValue);

        JClassType loopVariableType = inExpressionType.getTypeArgs()[0];
        VariableInfo variableInfo = context.addLocalVariable(loopVariableType, loopVariableName);
        loopVariableName = variableInfo.getJavaName();

        String inElementExpressionId =
            result.addCollectionExpression(inExpression.toString()) + COLLECTION_ARRAY_SUFFIX;

        return loopVariableName + " in " + inElementExpressionId;
    }

    private void renameLocalVariables(Expression expression, TemplateParserContext context)
    {
        if (expression instanceof NameExpr)
        {
            NameExpr nameExpr = ((NameExpr) expression);
            VariableInfo variableInfo = context.findVariableInContext(nameExpr.getNameAsString());
            if (variableInfo.isHasCustomJavaName())
            {
                nameExpr.setName(variableInfo.getJavaName());
            }
        }
        else if (expression instanceof MethodCallExpr)
        {
            for (Expression argument : ((MethodCallExpr) expression).getArguments())
            {
                renameLocalVariables(argument, context);
            }
            Optional<Expression> scope = ((MethodCallExpr) expression).getScope();
            scope.ifPresent(expression1 -> renameLocalVariables(expression1, context));
        }
        else if (expression instanceof BinaryExpr)
        {
            BinaryExpr binaryExpr = ((BinaryExpr) expression);
            renameLocalVariables(binaryExpr.getLeft(), context);
            renameLocalVariables(binaryExpr.getRight(), context);
        }
        else if (expression instanceof CastExpr)
        {
            renameLocalVariables(((CastExpr) expression).getExpression(), context);
        }
        else if (expression instanceof ConditionalExpr)
        {
            ConditionalExpr conditionalExpr = (ConditionalExpr) expression;
            renameLocalVariables(conditionalExpr.getCondition(), context);
            renameLocalVariables(conditionalExpr.getThenExpr(), context);

            if (conditionalExpr.getElseExpr() != null)
                renameLocalVariables(conditionalExpr.getElseExpr(), context);
        }
    }

    private String addExpressionToTemplate(String expStr, TemplateParserResult result)
    {
        return result.addTemplateExpression(expStr);
    }

    /**
     * Evaluate an expression type based on a given context
     * Only support method calls, field access and simple variable name
     * @param expression The Expression to evaluate the type of
     * @param context Context with declared variables
     * @return The type of the expression
     */
    private JType getVForExpressionType(Expression expression, TemplateParserContext context)
    {
        if (expression instanceof NameExpr)
        {
            VariableInfo variableInfo =
                context.findVariableInContext(((NameExpr) expression).getNameAsString());
            if (variableInfo != null)
                return variableInfo.getType();

            throw new InvalidExpressionException(
                "Couldn't find field " + ((NameExpr) expression).getName());
        }
        else if (expression instanceof MethodCallExpr)
        {
            // Evaluate method call
            MethodCallExpr methodCallExpr = (MethodCallExpr) expression;
            Optional<Expression> scope = methodCallExpr.getScope();
            if (!scope.isPresent())
            {
                // No scope
                throw new InvalidExpressionException("No scope for method call");
            }

            // Get the list of arguments
            List<JType> argumentTypes = new LinkedList<>();
            for (Expression argument : methodCallExpr.getArguments())
            {
                argumentTypes.add(getVForExpressionType(argument, context));
            }

            // Get the type for the parent expression
            JType scopeType = getVForExpressionType(scope.get(), context);

            // Try to find this method on it
            if (scopeType instanceof JClassType)
            {
                JClassType scopeClassType = ((JClassType) scopeType);
                JMethod method = scopeClassType.findMethod(methodCallExpr.getNameAsString(),
                    (JType[]) argumentTypes.toArray()
                );
                if (method != null)
                    return method.getReturnType();
            }

            throw new InvalidExpressionException(
                "Couldn't find method " + methodCallExpr.getName());
        }
        else if (expression instanceof FieldAccessExpr)
        {
            // Evaluate field access
            FieldAccessExpr fieldAccessExpr = (FieldAccessExpr) expression;
            Optional<Expression> scope = fieldAccessExpr.getScope();
            if (!scope.isPresent())
            {
                // No scope
                throw new InvalidExpressionException("No scope for field access");
            }

            JType parentType = getVForExpressionType(scope.get(), context);

            if (parentType instanceof JClassType)
            {
                for (JField jField : ((JClassType) parentType).getFields())
                {
                    if (jField.getName().equals(fieldAccessExpr.getNameAsString()))
                        return jField.getType();
                }
            }

            throw new InvalidExpressionException(
                "Couldn't find field " + fieldAccessExpr.getName());
        }

        throw new InvalidExpressionException("Unsupported expression.");
    }
}
