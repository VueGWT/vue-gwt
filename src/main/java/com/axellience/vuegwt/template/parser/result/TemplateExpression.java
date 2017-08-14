package com.axellience.vuegwt.template.parser.result;

import com.axellience.vuegwt.client.component.template.TemplateExpressionKind;
import com.axellience.vuegwt.template.parser.variable.VariableInfo;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * A Java expression from the template.
 * For each Java expression in the template, a {@link TemplateExpression} is created.
 * The Java expression from the template is then replaced by the result of {@link
 * TemplateExpression#toTemplateString()}.
 * This expression will be either by a computed property or a Method in the Vue.js Component.
 * @author Adrien Baron
 */
public class TemplateExpression
{
    private final String id;
    private final String body;
    private final TemplateExpressionKind kind;
    private final String type;
    private final List<VariableInfo> parameters = new LinkedList<>();

    public TemplateExpression(String id, String body, String type,
        Collection<VariableInfo> parameters, TemplateExpressionKind kind)
    {
        this.id = id;
        this.type = type;
        this.body = body;
        this.kind = kind;
        this.parameters.addAll(parameters);
    }

    /**
     * The kind of the expression, can be either a {@link TemplateExpressionKind#COMPUTED_PROPERTY}
     * (cached) or a {@link TemplateExpressionKind#METHOD} (not cached).
     * @return The kind for this expression
     */
    public TemplateExpressionKind getKind()
    {
        return kind;
    }

    /**
     * Return the ID of this expression. It's the name used in the template for it.
     * @return The id as a String
     */
    public String getId()
    {
        return id;
    }

    /**
     * Java type of the expression.
     * @return The fully qualified name of the returned Java type
     */
    public String getType()
    {
        return type;
    }

    /**
     * List of parameters this expression depends upon
     * @return The list of parameters for this expression
     */
    public List<VariableInfo> getParameters()
    {
        return parameters;
    }

    /**
     * The body of the expression. This is what was in the template and that must be returned by
     * this expression in Java.
     * @return The body of the expression
     */
    public String getBody()
    {
        return body;
    }

    /**
     * Return this expression as a string that can be placed in the template as a replacement
     * of the Java expression.
     * @return An expression that can be interpreted by Vue.js
     */
    public String toTemplateString()
    {
        if (getKind() == TemplateExpressionKind.COMPUTED_PROPERTY)
            return this.getId();

        String[] parametersName =
            this.parameters.stream().map(VariableInfo::getName).toArray(String[]::new);

        return this.getId() + "(" + String.join(", ", parametersName) + ")";
    }
}
