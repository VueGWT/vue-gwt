package com.axellience.vuegwt.client.component.template;

/**
 * @author Adrien Baron
 */
public class TemplateExpressionBase
{
    private final TemplateExpressionKind kind;
    private final String id;

    public TemplateExpressionBase(TemplateExpressionKind kind, String id)
    {
        this.kind = kind;
        this.id = id;
    }

    public TemplateExpressionKind getKind()
    {
        return kind;
    }

    public String getId()
    {
        return id;
    }
}
