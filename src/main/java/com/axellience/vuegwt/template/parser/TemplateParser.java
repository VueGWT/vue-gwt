package com.axellience.vuegwt.template.parser;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.template.TemplateExpressionKind;
import com.axellience.vuegwt.client.component.template.TemplateResource;
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
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.google.gwt.core.ext.typeinfo.JClassType;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * @param templateResourceClass The generated {@link TemplateResource} class of the {@link
     * VueComponent} we are processing
     * @return A {@link TemplateParserResult} containing the processed template, expressions and styles
     */
    public TemplateParserResult parseHtmlTemplate(String htmlTemplate,
        JClassType templateResourceClass)
    {
        result = new TemplateParserResult();
        Parser parser = Parser.htmlParser();
        parser.settings(new ParseSettings(true, true)); // tag, attribute preserve case
        Document doc = parser.parseInput(htmlTemplate, "");

        context = new TemplateParserContext(templateResourceClass);
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
            String processedExpression = processJavaExpression(expressionString).toTemplateString();
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

            currentExpressionReturnType = getExpressionReturnTypeForAttribute(attribute);
            attribute.setValue(processJavaExpression(attribute.getValue()).toTemplateString());
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

        return "Object";
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

        resolveCastsUsingImports(expression);
        resolveStaticMethodsUsingImports(expression);

        // Find the parameters used by the expression
        List<VariableInfo> expressionParameters = new LinkedList<>();
        findExpressionParameters(expression, expressionParameters);

        // Update the expression as it might have been changed
        expressionString = expression.toString();

        // If there is a cast first, we use this as the type of our expression
        if (expression instanceof CastExpr)
        {
            CastExpr castExpr = (CastExpr) expression;
            currentExpressionReturnType = castExpr.getType().toString();
        }

        TemplateExpressionKind expressionKind =
            getExpressionKind(expression, currentExpressionReturnType, expressionParameters);

        // Add the resulting expression to our template expressions
        return result.addExpression(expressionString,
            currentExpressionReturnType,
            expressionParameters,
            expressionKind);
    }

    /**
     * Resolve all the Cast in the expression.
     * This will replace the Class with the full qualified name using the template imports.
     * @param expression A Java expression from the Template
     */
    private void resolveCastsUsingImports(Expression expression)
    {
        // Resolve Cast types based on imports
        if (expression instanceof CastExpr)
        {
            CastExpr castExpr = ((CastExpr) expression);
            castExpr.setType(getCastType(castExpr));
        }

        // Recurse downward in the expression
        expression
            .getChildNodes()
            .stream()
            .filter(Expression.class::isInstance)
            .map(Expression.class::cast)
            .forEach(this::resolveCastsUsingImports);
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
     * Return the kind of our expression. Depending on how it used, and what the expression needs
     * we can either declare it as a Vue.js Computed property (with cache), or a Vue.js method
     * (without cache).
     * @param expression The expression to check
     * @param returnType The Java return type of the expression
     * @param expressionParameters The parameters needed for the expression (loop variables)
     * @return The {@link TemplateExpressionKind} of the expression.
     */
    private TemplateExpressionKind getExpressionKind(Expression expression, String returnType,
        List<VariableInfo> expressionParameters)
    {
        // If our expression returns void (used in a v-on) or depends on some parameters, then it's a method call.
        if (returnType.equals("void") || !expressionParameters.isEmpty())
            return TemplateExpressionKind.METHOD;

        // Check if we use any methods from the component, if so, expression is a method call.
        if (doesExpressionUsesComponentMethod(expression))
        {
            return TemplateExpressionKind.METHOD;
        }

        // Our expression can be a Computed Property!
        return TemplateExpressionKind.COMPUTED_PROPERTY;
    }

    /**
     * Check the expression to for component method calls.
     * This will check that the methods used in the template exist in the Component.
     * It returns true if we use one of the component method in the template.
     * It throws an exception if we use a method that is not declared in our Component.
     * This will not check for the type or number of parameters, we leave that to the Java Compiler.
     * @param expression The expression to check
     * @return True if we use at least one of the Component methods in the expression.
     */
    private boolean doesExpressionUsesComponentMethod(Expression expression)
    {
        boolean useMethod = false;
        if (expression instanceof MethodCallExpr)
        {
            MethodCallExpr methodCall = ((MethodCallExpr) expression);
            if (!methodCall.getScope().isPresent())
            {
                String methodName = methodCall.getName().getIdentifier();
                if (context.hasMethod(methodName))
                {
                    useMethod = true;
                }
                else if (!context.hasStaticMethod(methodName))
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
            useMethod = doesExpressionUsesComponentMethod(childExpr) || useMethod;
        }

        return useMethod;
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
            throw new TemplateExpressionException("Couldn't find variable \""
                + name
                + "\" in the Component.\nMake sure you didn't forget the @JsProperty annotation or try rerunning your Annotation processor.",
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
}
