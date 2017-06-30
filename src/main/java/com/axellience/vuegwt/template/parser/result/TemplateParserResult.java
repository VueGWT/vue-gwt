package com.axellience.vuegwt.template.parser.result;

import com.axellience.vuegwt.client.gwtextension.TemplateExpressionKind;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
    private final Map<String, String> styleImports = new HashMap<>();

    public String getTemplateWithReplacements()
    {
        return templateWithReplacements;
    }

    public void setTemplateWithReplacements(String templateWithReplacements)
    {
        this.templateWithReplacements = templateWithReplacements;
    }

    public TemplateExpression addExpression(TemplateExpressionKind kind, String expression,
        String expressionType, Set<TemplateExpressionParameter> parameters)
    {
        String id = EXPRESSION_PREFIX + this.expressions.size() + EXPRESSION_SUFFIX;
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

    public void addStyleImports(String styleName, String className)
    {
        this.styleImports.put(styleName, className);
    }

    public Map<String, String> getStyleImports()
    {
        return styleImports;
    }
}
