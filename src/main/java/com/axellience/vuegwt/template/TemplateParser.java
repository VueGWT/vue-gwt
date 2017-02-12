package com.axellience.vuegwt.template;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.LiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.google.gwt.core.ext.typeinfo.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.axellience.vuegwt.client.gwtextension.TemplateResource.COLLECTION_ARRAY_SUFFIX;

/**
 * @author Adrien Baron
 */
public class TemplateParser
{
    private static Pattern VUE_ATTR_PATTERN     = Pattern.compile("^(v-|:|@).*");
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

                String processedExpression = processExpression(expressionString, context, result);
                newText.append("{{ ").append(processedExpression).append(" }}");
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
                attribute.setValue(processVForValue(attribute.getValue(), context, result));
            }

            // Iterate on element attributes
            for (Attribute attribute : node.attributes())
            {
                String attributeName = attribute.getKey();
                if ("v-for".equals(attributeName))
                    continue;

                if (!VUE_ATTR_PATTERN.matcher(attributeName).matches())
                    continue;

                if (attributeName.indexOf("@") == 0 || attributeName.indexOf("v-on:") == 0)
                    continue;

                attribute.setValue(processExpression(attribute.getValue(), context, result));
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

    /**
     * Process a template expression
     * Will rename local variables if necessary
     * @param expressionString The expression as it is in the HTML template
     * @param context The current context
     * @param result The result of the template parser
     * @return A processed expression, should be placed in the HTML in place of the original
     * expression
     */
    private String processExpression(String expressionString, TemplateParserContext context,
        TemplateParserResult result)
    {
        try
        {
            // Try to parse the expression as JSON
            JSONObject jsonObject = new JSONObject(expressionString);
            JSONObject targetObject = new JSONObject();
            String jsonString =
                processJSONExpression(jsonObject, targetObject, context, result).toString();

            // Transform the JSON object into a JS object
            return jsonString.replaceAll("\"<<VUE_GWT_JSON_KEY ", "'")
                .replaceAll(" VUE_GWT_JSON_KEY>>\"", "'")
                .replaceAll("\"<<VUE_GWT_JSON_VALUE ", "")
                .replaceAll(" VUE_GWT_JSON_VALUE>>\"", "");
        }
        catch (JSONException e)
        {
            // Ignore exception
        }

        return processJavaExpression(expressionString, context, result);
    }

    /**
     * Process all the keys in a given JSON object
     * @param jsonObject the JSON object to process
     * @param context The current context
     * @param result The result of the template parser
     * @return
     */
    private JSONObject processJSONExpression(JSONObject jsonObject, JSONObject targetObject,
        TemplateParserContext context, TemplateParserResult result)
    {
        for (String key : jsonObject.keySet())
        {
            Object value = jsonObject.get(key);
            String targetKey = "<<VUE_GWT_JSON_KEY " + key + " VUE_GWT_JSON_KEY>>";
            if (value instanceof JSONObject)
            {
                targetObject.put(
                    targetKey,
                    processJSONExpression((JSONObject) value, targetObject, context, result)
                );
            }
            else
            {
                targetObject.put(targetKey, "<<VUE_GWT_JSON_VALUE " +
                    processJavaExpression(value.toString(), context, result) +
                    " VUE_GWT_JSON_VALUE>>");
            }
        }
        return jsonObject;
    }

    /**
     * Process the given string as a Java expression.
     * @param expressionString A valid Java expression
     * @param context The current context
     * @param result The result of the template parser
     * @return A processed expression, should be placed in the HTML in place of the original
     * expression
     */
    private String processJavaExpression(String expressionString, TemplateParserContext context,
        TemplateParserResult result)
    {
        // If we are inside a context we might have to rename variables
        Expression expression = JavaParser.parseExpression(expressionString);

        if (context.isInContext())
        {
            renameLocalVariables(expression, context);
            expressionString = expression.toString();
        }
        return result.addExpression(expressionString);
    }

    /**
     * Process a V-for value
     * Determine the type of the collection that's being iterated. Throw an exception if it can't
     * determine the type.
     * It will also register the loop variable as a local variable in the context stack.
     * @param vForValue The value of the v-for attribute
     * @param context The current context
     * @param result The result of the template parser
     * @return A processed v-for value, should be placed in the HTML in place of the original
     * v-for value
     */
    private String processVForValue(String vForValue, TemplateParserContext context,
        TemplateParserResult result)
    {
        String[] splitValue = vForValue.split(" in ");
        if (splitValue.length != 2)
            throw new InvalidExpressionException("Invalid v-for found: " + vForValue);

        String loopVariableName = splitValue[0];

        Expression inExpression = JavaParser.parseExpression(splitValue[1]);
        JParameterizedType inExpressionType =
            getExpressionType(inExpression, context).isParameterized();

        if (inExpressionType == null || inExpressionType.getTypeArgs().length != 1)
            throw new InvalidExpressionException("Couldn't determine v-for type: " + vForValue);

        JClassType loopVariableType = inExpressionType.getTypeArgs()[0];
        VariableInfo variableInfo = context.addLocalVariable(loopVariableType, loopVariableName);
        loopVariableName = variableInfo.getJavaName();

        if (context.isInContext())
        {
            renameLocalVariables(inExpression, context);
        }

        String inElementExpressionId = result.addCollectionExpression(inExpression.toString());
        return loopVariableName + " in " + inElementExpressionId + COLLECTION_ARRAY_SUFFIX;
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
            return;
        }
        else if (expression instanceof MethodCallExpr)
        {
            for (Expression argument : ((MethodCallExpr) expression).getArguments())
            {
                renameLocalVariables(argument, context);
            }
            Optional<Expression> scope = ((MethodCallExpr) expression).getScope();
            scope.ifPresent(expression1 -> renameLocalVariables(expression1, context));
            return;
        }
        else if (expression instanceof FieldAccessExpr)
        {
            // Evaluate field access
            FieldAccessExpr fieldAccessExpr = (FieldAccessExpr) expression;
            Optional<Expression> scope = fieldAccessExpr.getScope();
            scope.ifPresent(expression1 -> renameLocalVariables(expression1, context));
            return;
        }
        else if (expression instanceof ConditionalExpr)
        {
            ConditionalExpr conditionalExpr = (ConditionalExpr) expression;
            renameLocalVariables(conditionalExpr.getCondition(), context);
            renameLocalVariables(conditionalExpr.getThenExpr(), context);

            if (conditionalExpr.getElseExpr() != null)
                renameLocalVariables(conditionalExpr.getElseExpr(), context);
            return;
        }
        else if (expression instanceof BinaryExpr)
        {
            BinaryExpr binaryExpr = (BinaryExpr) expression;
            renameLocalVariables(binaryExpr.getRight(), context);
            renameLocalVariables(binaryExpr.getLeft(), context);
            return;
        }
        else if (expression instanceof LiteralExpr)
        {
            return;
        }

        throw new InvalidExpressionException(
            "Unsupported expression: " + expression.getClass().getCanonicalName() + " -> " +
                expression);
    }

    /**
     * Evaluate an expression type based on a given context
     * Only support method calls, field access and simple variable name
     * @param expression The Expression to evaluate the type of
     * @param context Context with declared variables
     * @return The type of the expression
     */
    private JType getExpressionType(Expression expression, TemplateParserContext context)
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

            // Get the type of the scope, if no scope, scope is the component
            JType scopeType = scope.isPresent() ? getExpressionType(scope.get(), context) :
                context.getVueComponentClass();

            // Get the list of arguments
            List<JType> argumentTypes = new LinkedList<>();
            for (Expression argument : methodCallExpr.getArguments())
            {
                argumentTypes.add(getExpressionType(argument, context));
            }

            JType[] argumentTypesArr =
                argumentTypes.isEmpty() ? new JType[0] : (JType[]) argumentTypes.toArray();

            // Try to find this method on it
            if (scopeType instanceof JClassType)
            {
                JClassType scopeClassType = ((JClassType) scopeType);
                JMethod method =
                    scopeClassType.findMethod(methodCallExpr.getNameAsString(), argumentTypesArr);
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

            JType parentType = getExpressionType(scope.get(), context);

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

        throw new InvalidExpressionException("Couldn't determine expression type: " + expression);
    }
}
