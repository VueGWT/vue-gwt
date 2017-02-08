package com.axellience.vuegwt.template;

import com.google.gwt.core.ext.typeinfo.JClassType;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Adrien Baron
 */
public class TemplateParser
{
    private static Pattern VUE_ATTR_PATTERN = Pattern.compile("^(v-|:|@).*");
    private static Pattern VUE_MUSTACHE_PATTERN = Pattern.compile("\\{\\{.*?}}");

    public TemplateParserResult parseHtmlTemplate(String htmlTemplate, JClassType vueComponentClass)
    {
        TemplateParserResult result = new TemplateParserResult();
        Parser parser = Parser.htmlParser();
        parser.settings(new ParseSettings(true, true)); // tag, attribute preserve case
        Document doc = parser.parseInput(htmlTemplate, "");

        processElement(doc, result);

        result.setTemplateWithReplacements(doc.body().html());
        return result;
    }

    private void processElement(Node node, TemplateParserResult result)
    {
        // Deal with mustache in the Nodes text
        if (node instanceof TextNode)
        {
            String elementText = ((TextNode) node).text();

            Matcher matcher = VUE_MUSTACHE_PATTERN.matcher(elementText);

            int lastEnd = 0;
            StringBuilder newText = new StringBuilder();
            while (matcher.find())
            {
                int start = matcher.start();
                int end = matcher.end();
                if (start > 0)
                    newText.append(elementText.substring(lastEnd, start));

                String expressionString = elementText.substring(start + 2, end - 2).trim();

                String expressionId = addExpressionToTemplate(expressionString, result);
                newText.append("{{ ").append(expressionId).append(" }}");
                lastEnd = end;
            }
            if (lastEnd > 0)
            {
                newText.append(elementText.substring(lastEnd));
                ((TextNode) node).text(newText.toString());
            }
        }
        else if (node instanceof Element)
        {
            // Iterate on element attributes
            for (Attribute attribute : node.attributes())
            {
                if ("v-for".equals(attribute.getKey()))
                    continue;

                if (!VUE_ATTR_PATTERN.matcher(attribute.getKey()).matches())
                    continue;

                if (":class".equals(attribute.getKey()) ||
                    "v-bind:class".equals(attribute.getKey()))
                    continue;

                String expressionString = attribute.getValue();

                attribute.setValue(addExpressionToTemplate(expressionString, result));
            }
        }

        // Recurse downwards
        node.childNodes().
            forEach(child -> processElement(child, result));
    }

    private String addExpressionToTemplate(String expStr, TemplateParserResult result)
    {
        return result.addTemplateExpression(expStr);
    }
}
