package com.axellience.vuegwt.template.parser;

import com.axellience.vuegwt.client.gwtextension.TemplateExpressionKind;
import com.axellience.vuegwt.template.parser.context.ComputedVariableInfo;
import com.axellience.vuegwt.template.parser.context.LocalVariableInfo;
import com.axellience.vuegwt.template.parser.context.TemplateParserContext;
import com.axellience.vuegwt.template.parser.context.VariableInfo;
import com.axellience.vuegwt.template.parser.result.TemplateExpression;
import com.axellience.vuegwt.template.parser.result.TemplateExpressionParameter;
import com.axellience.vuegwt.template.parser.result.TemplateParserResult;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.google.gwt.core.ext.typeinfo.JClassType;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        processImports(doc, parserContext, result);
        processNode(doc, parserContext, result);

        result.setTemplateWithReplacements(doc.body().html());
        return result;
    }

    /**
     * Process java imports in the template
     * @param doc The document to process
     * @param context Context of the parser
     */
    private void processImports(Document doc, TemplateParserContext context,
        TemplateParserResult result)
    {
        Set<Element> importElements = new HashSet<>();
        for (Element element : doc.getAllElements())
        {
            if (!"vue-gwt:import".equals(element.tagName()))
                continue;

            if (element.hasAttr("style"))
            {
                result.addStyleImports(element.attr("name"), element.attr("style"));
                context.addRootVariable(element.attr("style"), element.attr("name"));
            }
            else if (element.hasAttr("class"))
            {
                context.addImport(element.attr("class"));
            }

            importElements.add(element);
        }

        // Remove imports from the template once processed
        for (Element importElement : importElements)
        {
            importElement.remove();
        }
    }

    /**
     * Recursive method that will process the whole view DOM tree
     * @param node Current node being processed
     * @param context Context of the parser
     * @param result Result of the template parsing
     */
    private void processNode(Node node, TemplateParserContext context, TemplateParserResult result)
    {
        boolean shouldPopContextLayer = false;
        context.setCurrentNode(node);
        if (node instanceof TextNode)
        {
            processTextNode((TextNode) node, context, result);
        }
        else if (node instanceof Element)
        {
            shouldPopContextLayer = processElementNode((Element) node, context, result);
        }

        // Recurse downwards
        node.childNodes().
            forEach(child -> processNode(child, context, result));

        // After recursing downward, pop the context layer
        if (shouldPopContextLayer)
        {
            context.popContextLayer();
        }
    }

    /**
     * Process text node to check for {{ }} vue expressions
     * @param node Current node being processed
     * @param context Context of the parser
     * @param result Result of the template parsing
     */
    private void processTextNode(TextNode node, TemplateParserContext context,
        TemplateParserResult result)
    {
        String elementText = node.text();

        Matcher matcher = VUE_MUSTACHE_PATTERN.matcher(elementText);

        int lastEnd = 0;
        StringBuilder newText = new StringBuilder();
        while (matcher.find())
        {
            int start = matcher.start();
            int end = matcher.end();
            if (start > 0)
                newText.append(elementText.substring(lastEnd, start));

            context.setCurrentExpressionKind(TemplateExpressionKind.COMPUTED_PROPERTY);
            context.setCurrentExpressionReturnType("String");
            String expressionString = elementText.substring(start + 2, end - 2).trim();
            String processedExpression = processExpression(expressionString, context, result);
            newText.append("{{ ").append(processedExpression).append(" }}");
            lastEnd = end;
        }
        if (lastEnd > 0)
        {
            newText.append(elementText.substring(lastEnd));
            node.text(newText.toString());
        }
    }

    /**
     * Process Element node to check for vue attributes
     * @param element Current node being processed
     * @param context Context of the parser
     * @param result Result of the template parsing
     */
    private boolean processElementNode(Element element, TemplateParserContext context,
        TemplateParserResult result)
    {
        boolean shouldPopContext = false;

        // First try to find v-for
        for (Attribute attribute : element.attributes())
        {
            if (!"v-for".equals(attribute.getKey()))
                continue;

            // Add a context
            context.addContextLayer();
            shouldPopContext = true;

            // Process the for expression and declare local variables if necessary
            attribute.setValue(processVForValue(attribute.getValue(), context, result));
        }

        // Iterate on element attributes
        for (Attribute attribute : element.attributes())
        {
            String attributeName = attribute.getKey().toLowerCase();

            if ("v-for".equals(attributeName) || "v-model".equals(attributeName))
                continue;

            if (!VUE_ATTR_PATTERN.matcher(attributeName).matches())
                continue;

            context.setCurrentExpressionReturnType("Object");
            context.setCurrentExpressionKind(TemplateExpressionKind.COMPUTED_PROPERTY);

            if (attributeName.indexOf("@") == 0 || attributeName.indexOf("v-on:") == 0)
            {
                context.setCurrentExpressionReturnType("void");
                context.setCurrentExpressionKind(TemplateExpressionKind.METHOD);
            }

            if ("v-if".equals(attributeName) || "v-show".equals(attributeName))
            {
                context.setCurrentExpressionReturnType("boolean");
            }

            if ((":class".equals(attributeName) || "v-bind:class".equals(attributeName)) && isJSON(
                attribute.getValue()))
            {
                context.setCurrentExpressionReturnType("boolean");
            }

            if ((":style".equals(attributeName) || "v-bind:style".equals(attributeName)) && isJSON(
                attribute.getValue()))
            {
                context.setCurrentExpressionReturnType("String");
            }

            attribute.setValue(processExpression(attribute.getValue(), context, result));
        }

        return shouldPopContext;
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
        if ("".equals(expressionString))
            return "";

        if (expressionString.startsWith("[") && expressionString.endsWith("]"))
            return processArrayExpression(expressionString, context, result);

        // Try to parse the expression as JSON
        try
        {
            JSONObject jsonObject = new JSONObject(expressionString);
            String jsonString = processJSONExpression(jsonObject, context, result).toString();

            // Transform the JSON object into a JS object
            return jsonString
                .replaceAll("\"<<VUE_GWT_JSON_KEY ", "'")
                .replaceAll(" VUE_GWT_JSON_KEY>>\"", "'")
                .replaceAll("\"<<VUE_GWT_JSON_VALUE ", "")
                .replaceAll(" VUE_GWT_JSON_VALUE>>\"", "");
        }
        catch (JSONException ignore)
        {
            // Ignore exception
        }

        // Process as a JavaExpression instead
        return processJavaExpression(expressionString, context, result).toTemplateString();
    }

    private String processArrayExpression(String expressionString, TemplateParserContext context,
        TemplateParserResult result)
    {
        String arrayContent = Stream
            .of(expressionString.substring(1, expressionString.length() - 1).split(","))
            .map(exp -> this.processExpression(exp, context, result))
            .collect(Collectors.joining(","));

        return "[" + arrayContent + "]";
    }

    /**
     * Process all the keys in a given JSON object
     * @param jsonObject the JSON object to process
     * @param context The current context
     * @param result The result of the template parser
     * @return
     */
    private JSONObject processJSONExpression(JSONObject jsonObject, TemplateParserContext context,
        TemplateParserResult result)
    {
        JSONObject resultJson = new JSONObject();
        for (String key : jsonObject.keySet())
        {
            Object value = jsonObject.get(key);
            String targetKey = "<<VUE_GWT_JSON_KEY " + key + " VUE_GWT_JSON_KEY>>";
            if (value instanceof JSONObject)
            {
                resultJson.put(targetKey,
                    processJSONExpression((JSONObject) value, context, result));
            }
            else
            {
                resultJson.put(targetKey,
                    "<<VUE_GWT_JSON_VALUE " + processJavaExpression(value.toString(),
                        context,
                        result).toTemplateString() + " VUE_GWT_JSON_VALUE>>");
            }
        }
        return resultJson;
    }

    /**
     * Process the given string as a Java expression.
     * @param expressionString A valid Java expression
     * @param context The current context
     * @param result The result of the template parser
     * @return A processed expression, should be placed in the HTML in place of the original
     * expression
     */
    private TemplateExpression processJavaExpression(String expressionString,
        TemplateParserContext context, TemplateParserResult result)
    {
        Expression expression;
        try
        {
            expression = JavaParser.parseExpression(expressionString);
        }
        catch (ParseProblemException e)
        {
            throw new InvalidExpressionException("'"
                + expressionString
                + "' - Current Node: "
                + context.getCurrentNode());
        }

        // If there is a cast, we use this as type for our expression
        if (expression instanceof CastExpr)
        {
            CastExpr castExpr = (CastExpr) expression;
            context.setCurrentExpressionReturnType(getCastType(castExpr, context));
            expression = castExpr.getExpression();
        }

        // Rename variables if needed
        Set<TemplateExpressionParameter> parameters = new HashSet<>();
        processExpressionVariables(expression, context, parameters);
        expressionString = expression.toString();

        return result.addExpression(context.getCurrentExpressionKind(),
            expressionString,
            context.getCurrentExpressionReturnType(),
            parameters);
    }

    private String getCastType(CastExpr castExpr, TemplateParserContext context)
    {
        return context.getFullyQualifiedNameForClassName(castExpr.getType().toString());
    }

    /**
     * Process a v-for value
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
        VForDefinition vForDef = new VForDefinition(vForValue, context);

        // Set return of the "in" expression
        context.setCurrentExpressionReturnType(vForDef.getInExpressionType());
        context.setCurrentExpressionKind(TemplateExpressionKind.COMPUTED_PROPERTY);

        String inExpression = vForDef.getInExpression();

        // Process in expression if it's not just an "int"
        if (!"int".equals(vForDef.getInExpressionType()))
        {
            TemplateExpression templateExpression =
                this.processJavaExpression(inExpression, context, result);
            inExpression = templateExpression.toTemplateString();
        }

        // And return the newly built definition
        return vForDef.getVariableDefinition() + " in " + inExpression;
    }

    /**
     * Process all variable in the expression from the view
     * Replace loop variables and computed properties with the ones used in Java
     * @param expression An expression from the View
     * @param context The context containing all the local variables
     */
    private void processExpressionVariables(Expression expression, TemplateParserContext context,
        Set<TemplateExpressionParameter> parameters)
    {
        if (expression instanceof NameExpr)
        {
            NameExpr nameExpr = ((NameExpr) expression);
            if ("$event".equals(nameExpr.getNameAsString()))
            {
                processEventExpressionVariable(nameExpr, parameters);
            }
            else
            {
                processNameExpressionVariable(nameExpr, context, parameters);
            }
        }

        expression
            .getChildNodes()
            .stream()
            .filter(Expression.class::isInstance)
            .map(Expression.class::cast)
            .forEach(exp -> processExpressionVariables(exp, context, parameters));
    }

    /**
     * Process an event variable passed on v-on. This variable must have a valid cast in front.
     * @param nameExpr
     * @param parameters
     */
    private void processEventExpressionVariable(NameExpr nameExpr,
        Set<TemplateExpressionParameter> parameters)
    {
        if (nameExpr.getParentNode().isPresent() && nameExpr
            .getParentNode()
            .get() instanceof CastExpr)
        {
            CastExpr castExpr = (CastExpr) nameExpr.getParentNode().get();
            parameters.add(new TemplateExpressionParameter(castExpr.getType().toString(),
                "$event"));
        }
        else
        {
            throw new InvalidExpressionException(
                "$event should always be casted to it's intended type.");
        }
    }

    /**
     * Process name expression to replace loop variables and computed properties
     * with the ones used in Java
     * @param nameExpr
     * @param context
     * @param parameters
     */
    private void processNameExpressionVariable(NameExpr nameExpr, TemplateParserContext context,
        Set<TemplateExpressionParameter> parameters)
    {
        VariableInfo variableInfo = context.findVariableInContext(nameExpr.getNameAsString());
        if (variableInfo instanceof LocalVariableInfo)
        {
            LocalVariableInfo localVariableInfo = (LocalVariableInfo) variableInfo;
            nameExpr.setName(localVariableInfo.getGlobalName());
            parameters.add(new TemplateExpressionParameter(variableInfo.getType(),
                localVariableInfo.getGlobalName()));
        }
        else if (variableInfo instanceof ComputedVariableInfo)
        {
            nameExpr.setName(((ComputedVariableInfo) variableInfo).getGlobalName());
        }
    }

    private boolean isJSON(String value)
    {
        try
        {
            new JSONObject(value);
            return true;
        }
        catch (Exception ignored)
        {
            return false;
        }
    }
}
