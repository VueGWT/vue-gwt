package com.axellience.vuegwt.template.parser.result;

import com.axellience.vuegwt.client.gwtextension.TemplateExpressionKind;
import com.axellience.vuegwt.template.parser.context.VariableInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.axellience.vuegwt.client.gwtextension.TemplateResource.EXPRESSION_PREFIX;
import static com.axellience.vuegwt.client.gwtextension.TemplateResource.EXPRESSION_SUFFIX;

/**
 * @author Adrien Baron
 */
public class TemplateParserResult
{
    private String templateWithReplacements;
    private final List<TemplateExpression> expressions = new LinkedList<>();

    public String getTemplateWithReplacements()
    {
        return templateWithReplacements;
    }

    public void setTemplateWithReplacements(String templateWithReplacements)
    {
        this.templateWithReplacements = templateWithReplacements;
    }

    public TemplateExpression addCollectionExpression(String body, Set<VariableInfo> usedVariables)
    {
        return addExpression(TemplateExpressionKind.COLLECTION, body, "Object", usedVariables);
    }

    public TemplateExpression addExpression(TemplateExpressionKind kind, String expression,
        String expressionType, Set<VariableInfo> usedVariables)
    {
        String id = EXPRESSION_PREFIX + this.expressions.size() + EXPRESSION_SUFFIX;

        List<TemplateExpressionParameter> parameters = new LinkedList<>();
        for (VariableInfo usedVariable : usedVariables)
        {
            if (usedVariable.hasCustomJavaName())
            {
                parameters.add(new TemplateExpressionParameter(usedVariable
                    .getType()
                    .getQualifiedSourceName(), usedVariable.getJavaName()));
            }
        }

        if (kind == TemplateExpressionKind.COMPUTED_PROPERTY && !parameters.isEmpty())
            kind = TemplateExpressionKind.METHOD;

        TemplateExpression templateExpression =
            new TemplateExpression(kind, id, expression.trim(), expressionType, parameters);

        this.expressions.add(templateExpression);
        return templateExpression;
    }

    public List<TemplateExpression> getExpressions()
    {
        return expressions;
    }
}
