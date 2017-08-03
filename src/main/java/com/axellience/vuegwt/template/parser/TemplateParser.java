package com.axellience.vuegwt.template.parser;

import com.axellience.vuegwt.client.component.template.ComponentWithTemplate;
import com.axellience.vuegwt.template.parser.context.TemplateParserContext;
import com.axellience.vuegwt.template.parser.exceptions.TemplateExpressionException;
import com.axellience.vuegwt.template.parser.result.TemplateExpression;
import com.axellience.vuegwt.template.parser.result.TemplateParserResult;
import com.axellience.vuegwt.template.parser.variable.LocalVariableInfo;
import com.axellience.vuegwt.template.parser.variable.VariableInfo;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.google.gwt.core.ext.typeinfo.JClassType;
import jodd.json.JsonException;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Parse an HTML Vue GWT template.
 * <br>
 * This parser find all the Java expression and process them. It will throw explicit exceptions
 * if a variable cannot be find in the context.
 * It also automatically decide of the Java type of a given expression depending on the context
 * where it is used.
 * @author Adrien Baron
 */
public class TemplateParser
{
    private static Pattern VUE_ATTR_PATTERN = Pattern.compile("^(v-|:|@).*");
    private static Pattern VUE_MUSTACHE_PATTERN = Pattern.compile("\\{\\{.*?}}");

    private final JsonParser jsonParser;
    private final JsonSerializer jsonSerializer;

    private TemplateParserContext context;
    private TemplateParserResult result;

    private String currentExpressionReturnType;

    public TemplateParser()
    {
        this.jsonParser = new JsonParser();
        this.jsonParser.looseMode(true);

        this.jsonSerializer = new JsonSerializer();
        this.jsonSerializer.deep(true);
    }

    /**
     * Parse a given HTML template and return the a result object containing the expressions, styles
     * and a transformed HTML.
     * @param htmlTemplate The HTML template to process, as a String
     * @param componentWithTemplateClass The {@link ComponentWithTemplate} class of the component we
     * are processing
     * @return A {@link TemplateParserResult} containing the processed template, expressions and styles
     */
    public TemplateParserResult parseHtmlTemplate(String htmlTemplate,
        JClassType componentWithTemplateClass)
    {
        result = new TemplateParserResult();
        Parser parser = Parser.htmlParser();
        parser.settings(new ParseSettings(true, true)); // tag, attribute preserve case
        Document doc = parser.parseInput(htmlTemplate, "");

        context = new TemplateParserContext(componentWithTemplateClass);
        processImports(doc);
        processNode(doc);

        result.setProcessedTemplate(doc.body().html());
        return result;
    }

    /**
     * Add java imports in the template to the context.
     * @param doc The document to process
     */
    private void processImports(Document doc)
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
     * Recursive method that will process the whole template DOM tree.
     * @param node Current node being processed
     */
    private void processNode(Node node)
    {
        context.setCurrentNode(node);

        boolean nodeHasVFor = node.attributes().hasKey("v-for");
        if (nodeHasVFor)
        {
            // Add a context layer for our v-for
            context.addContextLayer();

            // Process the v-for expression, and update our attribute
            String processedVForValue = processVForValue(node.attr("v-for"));
            node.attr("v-for", processedVForValue);
        }

        if (node instanceof TextNode)
        {
            processTextNode((TextNode) node);
        }
        else if (node instanceof Element)
        {
            processElementNode((Element) node);
        }

        // Recurse downwards
        node.childNodes().
            forEach(this::processNode);

        if (nodeHasVFor)
        {
            // After downward recursion, pop the context layer
            context.popContextLayer();
        }
    }

    /**
     * Process text node to check for {{ }} vue expressions.
     * @param node Current node being processed
     */
    private void processTextNode(TextNode node)
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

            currentExpressionReturnType = "String";
            String expressionString = elementText.substring(start + 2, end - 2).trim();
            String processedExpression = processExpression(expressionString);
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
     * Process Element node to check for vue attributes.
     * @param element Current node being processed
     */
    private void processElementNode(Element element)
    {
        // Iterate on element attributes
        for (Attribute attribute : element.attributes())
        {
            String attributeName = attribute.getKey().toLowerCase();

            if ("v-for".equals(attributeName) || "v-model".equals(attributeName))
                continue;

            if (!VUE_ATTR_PATTERN.matcher(attributeName).matches())
                continue;

            currentExpressionReturnType = "Object";
            if (attributeName.indexOf("@") == 0 || attributeName.indexOf("v-on:") == 0)
            {
                currentExpressionReturnType = "void";
            }

            if ("v-if".equals(attributeName) || "v-show".equals(attributeName))
            {
                currentExpressionReturnType = "boolean";
            }

            if ((":class".equals(attributeName) || "v-bind:class".equals(attributeName))
                && isJSONObject(attribute.getValue()))
            {
                currentExpressionReturnType = "boolean";
            }

            if ((":style".equals(attributeName) || "v-bind:style".equals(attributeName))
                && isJSONObject(attribute.getValue()))
            {
                currentExpressionReturnType = "String";
            }

            attribute.setValue(processExpression(attribute.getValue()));
        }
    }

    /**
     * Process a template expression.
     * The expression can either be a JSON object, a JSON array or a Java expression.
     * @param expressionString The expression as it is in the HTML template
     * @return A processed expression, should be placed in the HTML in place of the original
     * expression
     */
    private String processExpression(String expressionString)
    {
        if ("".equals(expressionString))
            return "";

        if (isJSONArray(expressionString) || isJSONObject(expressionString))
        {
            try
            {
                // Try to parse the expression as JSON Object or Array
                Object mapOrList = jsonParser.parse(expressionString);
                return processMapOrList(mapOrList);
            }
            catch (JsonException e)
            {
                throw new TemplateExpressionException("Error while parsing JSON expression",
                    expressionString,
                    context,
                    e);
            }
        }

        return processJavaExpression(expressionString).toTemplateString();
    }

    /**
     * JSON object is parsed either as a map (for {}) or an List (for []).
     * @param mapOrList The map or list to process
     * @return A processed expression, should be placed in place of the original one
     */
    private String processMapOrList(Object mapOrList)
    {
        if (mapOrList instanceof List)
        {
            return processArrayExpression((List) mapOrList);
        }
        else if (mapOrList instanceof Map)
        {
            return processMapExpression((Map<String, Object>) mapOrList);
        }

        return processJavaExpression(mapOrList.toString()).toTemplateString();
    }

    /**
     * Process an array expression.
     * @param listOfExpression The list of expressions to process.
     * @return A processed JSON array, should be placed in place of the original one
     */
    private String processArrayExpression(List<Object> listOfExpression)
    {
        listOfExpression = listOfExpression
            .stream()
            .map(exp -> markValue(this.processMapOrList(exp)))
            .collect(Collectors.toList());

        return unquoteMarkedValues(jsonSerializer.serialize(listOfExpression));
    }

    /**
     * Process all the keys in a given JSON object.
     * @param map the JSON object to process
     * @return A processed JSON object, should be placed in place of the original one
     */
    private String processMapExpression(Map<String, Object> map)
    {
        Map<String, String> resultMap = new HashMap<>();
        for (String key : map.keySet())
        {
            resultMap.put(key, markValue(processMapOrList(map.get(key))));
        }

        return unquoteMarkedValues(jsonSerializer.serialize(resultMap));
    }

    /**
     * Process a v-for value.
     * It will register the loop variables as a local variable in the context stack.
     * @param vForValue The value of the v-for attribute
     * @return A processed v-for value, should be placed in the HTML in place of the original
     * v-for value
     */
    private String processVForValue(String vForValue)
    {
        VForDefinition vForDef = new VForDefinition(vForValue, context);

        // Set return of the "in" expression
        currentExpressionReturnType = vForDef.getInExpressionType();

        String inExpression = vForDef.getInExpression();

        // Process in expression if it's java
        if (vForDef.isInExpressionJava())
        {
            TemplateExpression templateExpression = this.processJavaExpression(inExpression);
            inExpression = templateExpression.toTemplateString();
        }

        // And return the newly built definition
        return vForDef.getVariableDefinition() + " in " + inExpression;
    }

    /**
     * Process the given string as a Java expression.
     * @param expressionString A valid Java expression
     * @return A processed expression, should be placed in the HTML in place of the original
     * expression
     */
    private TemplateExpression processJavaExpression(String expressionString)
    {
        Expression expression;
        try
        {
            expression = JavaParser.parseExpression(expressionString);
        }
        catch (ParseProblemException parseException)
        {
            throw new TemplateExpressionException(
                "Couldn't parse Expression, make sure it is valid Java.",
                expressionString,
                context,
                parseException);
        }

        // First, resolve all the casts
        resolveCasts(expression);

        // Find the parameters used by the expression
        Set<VariableInfo> expressionParameters = new HashSet<>();
        processNameExpressions(expression, expressionParameters);

        // Update the expression as it might have been changed
        expressionString = expression.toString();

        // If there is a cast first, we use this as the type of our expression
        if (expression instanceof CastExpr)
        {
            CastExpr castExpr = (CastExpr) expression;
            currentExpressionReturnType = castExpr.getType().toString();
        }

        // Add the resulting expression to our template expressions
        return result.addExpression(expressionString,
            currentExpressionReturnType,
            expressionParameters);
    }

    /**
     * Resolve all the Cast in the expression.
     * This will replace the Class with the full qualified name using the template imports.
     * @param expression A Java expression from the Template
     */
    private void resolveCasts(Expression expression)
    {
        // Resolve Cast types based on imports
        if (expression instanceof CastExpr)
        {
            CastExpr castExpr = ((CastExpr) expression);
            castExpr.setType(getCastType(castExpr));
        }

        expression
            .getChildNodes()
            .stream()
            .filter(Expression.class::isInstance)
            .map(Expression.class::cast)
            .forEach(this::resolveCasts);
    }

    /**
     * Process all the name expressions in our Expression.
     * This will find all the parameters this expression depends on.
     * This is either the local variables (from a v-for loop) or the $event variable.
     * @param expression An expression from the Template
     */
    private void processNameExpressions(Expression expression, Set<VariableInfo> parameters)
    {
        if (expression instanceof NameExpr)
        {
            NameExpr nameExpr = ((NameExpr) expression);
            if ("$event".equals(nameExpr.getNameAsString()))
                processEventParameter(expression, nameExpr, parameters);
            else
                processNameExpression(expression, nameExpr, parameters);
        }

        expression
            .getChildNodes()
            .stream()
            .filter(Expression.class::isInstance)
            .map(Expression.class::cast)
            .forEach(exp -> processNameExpressions(exp, parameters));
    }

    /**
     * Process the $event variable passed on v-on. This variable must have a valid cast in front.
     * @param expression The currently processed expression
     * @param nameExpr The variable we are processing
     * @param parameters A set of parameters for the current expression
     */
    private void processEventParameter(Expression expression, NameExpr nameExpr,
        Set<VariableInfo> parameters)
    {
        if (nameExpr.getParentNode().isPresent() && nameExpr
            .getParentNode()
            .get() instanceof CastExpr)
        {
            CastExpr castExpr = (CastExpr) nameExpr.getParentNode().get();
            parameters.add(new VariableInfo(castExpr.getType().toString(), "$event"));
        }
        else
        {
            throw new TemplateExpressionException(
                "$event should always be casted to it's intended type.",
                expression.toString(),
                context);
        }
    }

    /**
     * Process a name expression to determine if it exists in the context.
     * If it does, and it's a local variable (from a v-for) we add it to our parameters
     * @param expression The currently processed expression
     * @param nameExpr The variable we are processing
     * @param parameters A set of parameters for the current expression
     */
    private void processNameExpression(Expression expression, NameExpr nameExpr,
        Set<VariableInfo> parameters)
    {
        String name = nameExpr.getNameAsString();
        if (context.hasImport(name))
        {
            // This is a direct Class reference, we just replace with the fully qualified name
            nameExpr.setName(context.getFullyQualifiedNameForClassName(name));
            return;
        }

        VariableInfo variableInfo = context.findVariable(name);
        if (variableInfo == null)
        {
            throw new TemplateExpressionException("Couldn't find variable "
                + name
                + " in the Component. Make sure you didn't forget the @JsProperty annotation.",
                expression.toString(),
                context);
        }

        if (variableInfo instanceof LocalVariableInfo)
        {
            parameters.add(variableInfo);
        }
    }

    private String getCastType(CastExpr castExpr)
    {
        return context.getFullyQualifiedNameForClassName(castExpr.getType().toString());
    }

    private String markValue(String value)
    {
        return "<<$VUE_GWT_VALUE " + value + " $VUE_GWT_VALUE>>";
    }

    private String unquoteMarkedValues(String expression)
    {
        return expression
            .replaceAll("\"<<\\$VUE_GWT_VALUE ", "")
            .replaceAll(" \\$VUE_GWT_VALUE>>\"", "");
    }

    private boolean isJSONObject(String value)
    {
        return value.trim().startsWith("{");
    }

    private boolean isJSONArray(String value)
    {
        return value.trim().startsWith("[");
    }
}
