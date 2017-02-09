package com.axellience.vuegwt.template;

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
    private final Map<String, String> templateExpressions    = new HashMap<>();
    private final Map<String, String> collectionsExpressions = new HashMap<>();
    private final List<VariableInfo>  localVariables         = new LinkedList<>();

    public String getTemplateWithReplacements()
    {
        return templateWithReplacements;
    }

    public void setTemplateWithReplacements(String templateWithReplacements)
    {
        this.templateWithReplacements = templateWithReplacements;
    }

    public String addTemplateExpression(String expression)
    {
        String key = EXPRESSION_PREFIX + this.templateExpressions.size();
        this.templateExpressions.put(key, expression.trim());
        return key;
    }

    public String addCollectionExpression(String expression)
    {
        String id = COLLECTION_PREFIX + this.collectionsExpressions.size();
        this.collectionsExpressions.put(id, expression);
        return id;
    }

    public Map<String, String> getTemplateExpressions()
    {
        return templateExpressions;
    }

    public Map<String, String> getCollectionsExpressions()
    {
        return collectionsExpressions;
    }

    public void addLocalVariables(Collection<VariableInfo> variablesInfo)
    {
        this.localVariables.addAll(variablesInfo);
    }

    public List<VariableInfo> getLocalVariables()
    {
        return localVariables;
    }
}
