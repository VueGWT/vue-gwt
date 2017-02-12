package com.axellience.vuegwt.template;

import com.google.gwt.core.ext.typeinfo.JType;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.axellience.vuegwt.client.gwtextension.TemplateResource.COLLECTION_PREFIX;
import static com.axellience.vuegwt.client.gwtextension.TemplateResource.EXPRESSION_PREFIX;

/**
 * @author Adrien Baron
 */
public class TemplateParserResult
{
    private String templateWithReplacements;
    private final Map<String, ExpressionInfo> expressions            = new HashMap<>();
    private final Map<String, String>         collectionsExpressions = new HashMap<>();
    private final List<VariableInfo>          localVariables         = new LinkedList<>();

    public String getTemplateWithReplacements()
    {
        return templateWithReplacements;
    }

    public void setTemplateWithReplacements(String templateWithReplacements)
    {
        this.templateWithReplacements = templateWithReplacements;
    }

    public String addExpression(String expression)
    {
        String key = EXPRESSION_PREFIX + this.expressions.size();
        this.expressions.put(key, new ExpressionInfo(expression.trim()));
        return key;
    }

    public String addCollectionExpression(String expression)
    {
        String id = COLLECTION_PREFIX + this.collectionsExpressions.size();
        this.collectionsExpressions.put(id, expression.trim());
        return id;
    }

    public void addLocalVariables(Collection<VariableInfo> variablesInfo)
    {
        this.localVariables.addAll(variablesInfo);
    }

    public Map<String, ExpressionInfo> getExpressions()
    {
        return expressions;
    }

    public Map<String, String> getCollectionsExpressions()
    {
        return collectionsExpressions;
    }

    public List<VariableInfo> getLocalVariables()
    {
        return localVariables;
    }
}
