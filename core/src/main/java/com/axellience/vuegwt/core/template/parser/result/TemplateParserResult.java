package com.axellience.vuegwt.core.template.parser.result;

import com.axellience.vuegwt.core.template.parser.variable.VariableInfo;

import java.util.LinkedList;
import java.util.List;

import static com.axellience.vuegwt.core.client.template.ComponentTemplate.EXPRESSION_PREFIX;

/**
 * Result of a template parsing.
 * @author Adrien Baron
 */
public class TemplateParserResult
{
    private String processedTemplate;
    private final List<TemplateExpression> expressions = new LinkedList<>();

    /**
     * Set the processed template, once all the Java expression has been replaced by
     * methods/computed properties.
     * @param processedTemplate The processed template
     */
    public void setProcessedTemplate(String processedTemplate)
    {
        this.processedTemplate = processedTemplate;
    }

    /**
     * Get the processed template, once all the Java expression has been replaced by
     * methods/computed properties.
     * @return The processed template
     */
    public String getProcessedTemplate()
    {
        return processedTemplate;
    }

    /**
     * Add an expression to the result.
     * All the Java methods from the template will be added here so we can add them to our Vue.js
     * component.
     * @param expression The Java expression
     * @param expressionType The type of the expression, determined depending on the context it is
     * used in.
     * @param shouldCast Should the expression be cast to the given expressionType
     * @param parameters The parameters this expression depends on (can be empty)
     * @return The {@link TemplateExpression} for this Java expression, will be used to get the
     * string to put in the template instead.
     */
    public TemplateExpression addExpression(String expression, String expressionType, boolean shouldCast,
        List<VariableInfo> parameters)
    {
        String id = EXPRESSION_PREFIX + this.expressions.size();

        TemplateExpression templateExpression =
            new TemplateExpression(id, expression.trim(), expressionType, shouldCast, parameters);

        this.expressions.add(templateExpression);
        return templateExpression;
    }

    /**
     * Return the list of expression we found in the template.
     * @return The list of {@link TemplateExpression}
     */
    public List<TemplateExpression> getExpressions()
    {
        return expressions;
    }
}
