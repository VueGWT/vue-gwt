package com.axellience.vuegwt.core.template.parser;

import jsinterop.base.Any;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;

import com.axellience.vuegwt.core.template.parser.context.TemplateParserContext;
import com.axellience.vuegwt.core.template.parser.exceptions.TemplateExpressionException;
import com.axellience.vuegwt.core.template.parser.result.TemplateExpression;
import com.axellience.vuegwt.core.template.parser.result.TemplateParserResult;
import com.axellience.vuegwt.core.template.parser.variable.LocalVariableInfo;
import com.axellience.vuegwt.core.template.parser.variable.VariableInfo;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithType;
import com.github.javaparser.ast.type.Type;
import com.google.gwt.core.ext.TreeLogger;

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
    private final TreeLogger logger;
    private static Pattern VUE_ATTR_PATTERN = Pattern.compile("^(v-|:|@).*");
    private static Pattern VUE_MUSTACHE_PATTERN = Pattern.compile("\\{\\{.*?}}");

    private TemplateParserContext context;
    private TemplateParserResult result;

    private Attribute currentAttribute;
    private String currentExpressionReturnType;

    public TemplateParser(TreeLogger logger)
    {
        this.logger = logger;
    }

    /**
     * Parse a given HTML template and return the a result object containing the expressions, styles
     * and a transformed HTML.
     * @param htmlTemplate The HTML template to process, as a String
     * @param context Context of the Component we are currently processing
     * @return A {@link TemplateParserResult} containing the processed template, expressions and styles
     */
    public TemplateParserResult parseHtmlTemplate(String htmlTemplate,
        TemplateParserContext context)
    {
        this.context = context;
        Parser parser = Parser.htmlParser();
        parser.settings(new ParseSettings(true, true)); // tag, attribute preserve case
        Document doc = parser.parseInput(htmlTemplate, "");

        result = new TemplateParserResult();
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
                logger.log(TreeLogger.WARN,
                    "Style import in template is deprecated and will be removed in beta-6. Found usage in "
                        + context.getTemplateName()
                        + ". Please see https://axellience.github.io/vue-gwt/gwt-integration/client-bundles-and-styles.html#styles for information on how to use styles without this import.");
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

            this.currentAttribute = attribute;
            currentExpressionReturnType = getExpressionReturnTypeForAttribute(attribute);
            attribute.setValue(processExpression(attribute.getValue()));
        }
    }

    /**
     * Guess the type of the expression based on where it is used.
     * The guessed type can be overridden by adding a Cast to the desired type at the
     * beginning of the expression.
     * @param attribute The attribute the expression is in
     * @return
     */
    private String getExpressionReturnTypeForAttribute(Attribute attribute)
    {
        String attributeName = attribute.getKey().toLowerCase();

        if (attributeName.indexOf("@") == 0 || attributeName.indexOf("v-on:") == 0)
            return "void";

        if ("v-if".equals(attributeName) || "v-show".equals(attributeName))
            return "boolean";

        return Any.class.getCanonicalName();
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
            inExpression = this.processExpression(inExpression);
        }

        // And return the newly built definition
        return vForDef.getVariableDefinition() + " in " + inExpression;
    }

    /**
     * Process a given template expression
     * @param expressionString Should be either empty or a valid Java expression
     * @return The processed expression
     */
    private String processExpression(String expressionString)
    {
        expressionString = expressionString.trim();
        if (expressionString.isEmpty())
        {
            if (isAttributeBinding(currentAttribute))
            {
                throw new TemplateExpressionException(
                    "Empty expression in template property binding. If you want to pass an empty string then simply don't use binding: my-attribute=\"\"",
                    currentAttribute.toString(),
                    context);
            }
            else if (isEventBinding(currentAttribute))
            {
                throw new TemplateExpressionException("Empty expression in template event binding.",
                    currentAttribute.toString(),
                    context);
            }
            else
            {
                return "";
            }
        }

        if (expressionString.startsWith("{"))
            throw new TemplateExpressionException(
                "Object literal syntax are not supported yet in Vue GWT, please use map(e(\"key1\", myValue), e(\"key2\", myValue2 > 5)...) instead.\nThe object returned by map() is a regular Javascript Object (JsObject) with the given key/values.",
                expressionString,
                context);

        if (expressionString.startsWith("["))
            throw new TemplateExpressionException(
                "Array literal syntax are not supported yet in Vue GWT, please use array(myValue, myValue2 > 5...) instead.\nThe object returned by array() is a regular Javascript Array (JsArray) with the given values.",
                expressionString,
                context);

        // We don't optimize String expression, as we want GWT to convert
        // Java values to String for us (Enums, wrapped primitives...)
        if (!"String".equals(currentExpressionReturnType) && isSimpleVueJsExpression(
            expressionString))
            return expressionString;

        return processJavaExpression(expressionString).toTemplateString();
    }

    private boolean isAttributeBinding(Attribute attribute)
    {
        String attributeName = attribute.getKey().toLowerCase();
        return attributeName.startsWith(":") || attributeName.startsWith("v-bind:");
    }

    private boolean isEventBinding(Attribute attribute)
    {
        String attributeName = attribute.getKey().toLowerCase();
        return attributeName.startsWith("@") || attributeName.startsWith("v-on:");
    }

    /**
     * In some case the expression is already a valid Vue.js expression that will work without
     * any processing. In this case we just leave it in place.
     * This avoid creating Computed properties/methods for simple expressions.
     * @param expressionString The expression to check
     * @return true if it's already a valid Vue.js expression, false otherwise
     */
    private boolean isSimpleVueJsExpression(String expressionString)
    {
        String methodName = expressionString;
        if (expressionString.endsWith("()"))
            methodName = expressionString.substring(0, expressionString.length() - 2);

        // Just a method name/simple method call with no parameters
        if (context.hasMethod(methodName))
            return true;

        // Just a variable
        return context.findVariable(expressionString) != null;
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

        resolveTypesUsingImports(expression);
        resolveStaticMethodsUsingImports(expression);

        checkMethodNames(expression);

        // Find the parameters used by the expression
        List<VariableInfo> expressionParameters = new LinkedList<>();
        findExpressionParameters(expression, expressionParameters);

        // If there is a cast first, we use this as the type of our expression
        expression = getTypeFromCast(expression);

        // Update the expression as it might have been changed
        expressionString = expression.toString();

        // Add the resulting expression to our result
        return result.addExpression(expressionString,
            currentExpressionReturnType,
            expressionParameters);
    }

    /**
     * Get the type of an expression from the cast at the beginning.
     * (int) 12 -> 12 of type int
     * (int) 15 + 5 -> 15 + 5 of type int
     * (float) (12 + 3) -> 12 + 3 of type float
     * ((int) 12) + 3 -> ((int) 12) + 3 of type Any
     * ((JsArray) myArray).getAt(0) -> ((JsArray) myArray).getAt(0) of type Any
     * @param expression The expression to process
     * @return The modified expression (where cast has been removed if necessary)
     */
    private Expression getTypeFromCast(Expression expression)
    {
        if (expression instanceof BinaryExpr)
        {
            Expression mostLeft = getLeftmostExpression(expression);
            if (mostLeft instanceof CastExpr)
            {
                CastExpr castExpr = (CastExpr) mostLeft;
                currentExpressionReturnType = castExpr.getType().toString();
                BinaryExpr parent = (BinaryExpr) mostLeft.getParentNode().get();
                parent.setLeft(castExpr.getExpression());
            }
        }
        else if (expression instanceof CastExpr)
        {
            CastExpr castExpr = (CastExpr) expression;
            currentExpressionReturnType = castExpr.getType().toString();
            expression = castExpr.getExpression();
        }
        return expression;
    }

    private Expression getLeftmostExpression(Expression expression)
    {
        if (expression instanceof BinaryExpr)
            return getLeftmostExpression(((BinaryExpr) expression).getLeft());

        return expression;
    }

    /**
     * Resolve all the types in the expression.
     * This will replace the Class with the full qualified name using the template imports.
     * @param expression A Java expression from the Template
     */
    private void resolveTypesUsingImports(Expression expression)
    {
        if (expression instanceof NodeWithType)
        {
            NodeWithType nodeWithType = ((NodeWithType) expression);
            nodeWithType.setType(getQualifiedName(nodeWithType.getType()));
        }

        // Recurse downward in the expression
        expression
            .getChildNodes()
            .stream()
            .filter(Expression.class::isInstance)
            .map(Expression.class::cast)
            .forEach(this::resolveTypesUsingImports);
    }

    /**
     * Resolve static method calls using static imports
     * @param expression The expression to resolve
     */
    private void resolveStaticMethodsUsingImports(Expression expression)
    {
        if (expression instanceof MethodCallExpr)
        {
            MethodCallExpr methodCall = ((MethodCallExpr) expression);
            String methodName = methodCall.getName().getIdentifier();
            if (!methodCall.getScope().isPresent() && context.hasStaticMethod(methodName))
            {
                methodCall.setName(context.getFullyQualifiedNameForMethodName(methodName));
            }
        }

        // Recurse downward in the expression
        expression
            .getChildNodes()
            .stream()
            .filter(Expression.class::isInstance)
            .map(Expression.class::cast)
            .forEach(this::resolveStaticMethodsUsingImports);
    }

    /**
     * Check the expression for component method calls.
     * This will check that the methods used in the template exist in the Component.
     * It throws an exception if we use a method that is not declared in our Component.
     * This will not check for the type or number of parameters, we leave that to the Java Compiler.
     * @param expression The expression to check
     */
    private void checkMethodNames(Expression expression)
    {
        if (expression instanceof MethodCallExpr)
        {
            MethodCallExpr methodCall = ((MethodCallExpr) expression);
            if (!methodCall.getScope().isPresent())
            {
                String methodName = methodCall.getName().getIdentifier();
                if (!context.hasMethod(methodName) && !context.hasStaticMethod(methodName))
                {
                    throw new TemplateExpressionException("Couldn't find the method \""
                        + methodName
                        + "\" in the Component."
                        + "\nMake sure it is not private or try rerunning your Annotation processor.",
                        expression.toString(),
                        context);
                }
            }
        }

        for (com.github.javaparser.ast.Node node : expression.getChildNodes())
        {
            if (!(node instanceof Expression))
                continue;

            Expression childExpr = (Expression) node;
            checkMethodNames(childExpr);
        }
    }

    /**
     * Find all the parameters this expression depends on.
     * This is either the local variables (from a v-for loop) or the $event variable.
     * @param expression An expression from the Template
     * @param parameters The parameters this expression depends on
     */
    private void findExpressionParameters(Expression expression, List<VariableInfo> parameters)
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
            .forEach(exp -> findExpressionParameters(exp, parameters));
    }

    /**
     * Process the $event variable passed on v-on. This variable must have a valid cast in front.
     * @param expression The currently processed expression
     * @param nameExpr The variable we are processing
     * @param parameters The parameters this expression depends on
     */
    private void processEventParameter(Expression expression, NameExpr nameExpr,
        List<VariableInfo> parameters)
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
                "\"$event\" should always be casted to it's intended type. Example: @click=\"doSomething((NativeEvent) $event)\".",
                expression.toString(),
                context);
        }
    }

    /**
     * Process a name expression to determine if it exists in the context.
     * If it does, and it's a local variable (from a v-for) we add it to our parameters
     * @param expression The currently processed expression
     * @param nameExpr The variable we are processing
     * @param parameters The parameters this expression depends on
     */
    private void processNameExpression(Expression expression, NameExpr nameExpr,
        List<VariableInfo> parameters)
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
            throw new TemplateExpressionException("Couldn't find variable/method \""
                + name
                + "\" in the Component.\nMake sure you didn't forget the @JsProperty/@JsMethod annotation or try rerunning your Annotation processor.",
                expression.toString(),
                context);
        }

        if (variableInfo instanceof LocalVariableInfo)
        {
            parameters.add(variableInfo);
        }
    }

    private String getQualifiedName(Type type)
    {
        return context.getFullyQualifiedNameForClassName(type.toString());
    }
}
