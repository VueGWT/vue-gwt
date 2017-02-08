package com.axellience.vuegwt.template;

import java.util.HashMap;
import java.util.Map;

import static com.axellience.vuegwt.client.gwtextension.TemplateResource.EXPRESSION_PREFIX;

/**
 * @author Adrien Baron
 */
public class TemplateParserResult
{
    String templateWithReplacements;
    Map<String, String> templateExpressions = new HashMap<>();

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

    public Map<String, String> getTemplateExpressions()
    {
        return templateExpressions;
    }
}
