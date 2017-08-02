package com.axellience.vuegwt.template.parser.result;

import com.axellience.vuegwt.client.component.template.TemplateExpressionBase;
import com.axellience.vuegwt.client.component.template.TemplateExpressionKind;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Adrien Baron
 */
public class TemplateExpression extends TemplateExpressionBase
{
    private final String body;
    private final String returnType;
    private final List<TemplateExpressionParameter> parameters = new LinkedList<>();

    public TemplateExpression(TemplateExpressionKind kind, String id, String body,
        String returnType, Collection<TemplateExpressionParameter> parameters)
    {
        super(kind, id);
        this.body = body;
        this.returnType = returnType;
        this.parameters.addAll(parameters);
    }

    public String getReturnType()
    {
        return returnType;
    }

    public List<TemplateExpressionParameter> getParameters()
    {
        return parameters;
    }

    public String getBody()
    {
        return body;
    }

    public String toTemplateString()
    {
        if (getKind() == TemplateExpressionKind.COMPUTED_PROPERTY)
            return this.getId();

        String[] parametersName = this.parameters
            .stream()
            .map(TemplateExpressionParameter::getName)
            .toArray(String[]::new);

        return this.getId() + "(" + String.join(", ", parametersName) + ")";
    }
}
