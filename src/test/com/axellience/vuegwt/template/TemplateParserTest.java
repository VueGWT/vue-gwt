package com.axellience.vuegwt.template;

import com.axellience.mockito.MockitoExtension;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static com.axellience.vuegwt.client.gwtextension.TemplateResource.EXPRESSION_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Adrien Baron
 */
@ExtendWith(MockitoExtension.class)
public class TemplateParserTest
{
    private TemplateParser templateParser;

    @BeforeEach
    void init()
    {
        templateParser = new TemplateParser();
    }

    @Test
    void parseBasicHtml(@Mock JClassType vueComponent)
    {
        String html = "<div class=\"test\"></div>";
        when(vueComponent.getFields()).thenReturn(new JField[] {});
        TemplateParserResult result = templateParser.parseHtmlTemplate(html, vueComponent);
        assertEquals(html, pr(result));
    }

    @Test
    void replaceVueExpression(@Mock JClassType vueComponent, @Mock JField test,
        @Mock JClassType testType)
    {
        String html = "<div v-bind:title=\"test\"></div>";

        when(test.getName()).thenReturn("test");
        when(test.getType()).thenReturn(testType);
        when(vueComponent.getFields()).thenReturn(new JField[] { test });

        TemplateParserResult result = templateParser.parseHtmlTemplate(html, vueComponent);

        assertEquals("<div v-bind:title=\"" + EXPRESSION_PREFIX + "0_EXPR\"></div>", pr(result));
        assertEquals(1, result.getExpressions().size());
        assertEquals(
            "test", result.getExpressions().get(0).getExpression());
    }

    @Test
    void supportAtSign(@Mock JClassType vueComponent, @Mock JField test, @Mock JClassType testType)
    {
        String html = "<div @class=\"test\"></div>";

        when(test.getName()).thenReturn("test");
        when(test.getType()).thenReturn(testType);
        when(vueComponent.getFields()).thenReturn(new JField[] { test });

        TemplateParserResult result = templateParser.parseHtmlTemplate(html, vueComponent);

        assertEquals("<div @class=\"" + EXPRESSION_PREFIX + "0_EXPR\"></div>", pr(result));
    }

    @Test
    void supportTwoDots(@Mock JClassType vueComponent, @Mock JField test, @Mock JClassType testType)
    {
        String html = "<div :title=\"test\"></div>";

        when(test.getName()).thenReturn("test");
        when(test.getType()).thenReturn(testType);
        when(vueComponent.getFields()).thenReturn(new JField[] { test });

        TemplateParserResult result = templateParser.parseHtmlTemplate(html, vueComponent);

        assertEquals("<div :title=\"" + EXPRESSION_PREFIX + "0_EXPR\"></div>", pr(result));
    }

    @Test
    void supportMustache(@Mock JClassType vueComponent, @Mock JField test,
        @Mock JClassType testType)
    {
        String html = "<div>{{ test }}</div>";

        when(test.getName()).thenReturn("test");
        when(test.getType()).thenReturn(testType);
        when(vueComponent.getFields()).thenReturn(new JField[] { test });

        TemplateParserResult result = templateParser.parseHtmlTemplate(html, vueComponent);

        assertEquals("<div>{{ " + EXPRESSION_PREFIX + "0_EXPR }}</div>", pr(result));
        assertEquals(1, result.getExpressions().size());
        assertEquals(
            "test", result.getExpressions().get(0).getExpression());
    }

    private String pr(TemplateParserResult result)
    {
        return result.getTemplateWithReplacements().replaceAll("\n *", "");
    }
}
