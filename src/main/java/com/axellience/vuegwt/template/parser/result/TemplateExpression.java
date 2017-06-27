package com.axellience.vuegwt.template.parser.result;

import com.axellience.vuegwt.client.gwtextension.TemplateExpressionBase;
import com.axellience.vuegwt.client.gwtextension.TemplateExpressionKind;

import java.util.LinkedList;
import java.util.List;

import static com.axellience.vuegwt.client.gwtextension.TemplateResource.COLLECTION_ARRAY_SUFFIX;

/**
 * @author Adrien Baron
 */
public class TemplateExpression extends TemplateExpressionBase
{
    private final String body;
    private final String returnType;
    private final List<TemplateExpressionParameter> parameters = new LinkedList<>();

    public TemplateExpression(TemplateExpressionKind kind, String id, String body,
        String returnType)
    {
        super(kind, id);
        this.body = body;
        this.returnType = returnType;
    }

    public void addParameter(TemplateExpressionParameter parameter)
    {
        parameters.add(parameter);
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
        if (getKind() == TemplateExpressionKind.COLLECTION)
            return this.getId() + COLLECTION_ARRAY_SUFFIX;

        String[] parametersName = this.parameters
            .stream()
            .map(TemplateExpressionParameter::getName)
            .toArray(String[]::new);

        return this.getId() + "(" + String.join(", ", parametersName) + ")";
    }
}
