package com.axellience.vuegwt.template;

import com.axellience.mockito.MockitoExtension;
import com.google.gwt.core.ext.typeinfo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static com.axellience.vuegwt.client.gwtextension.TemplateResource.EXPRESSION_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
    void replaceVueExpression(@Mock JClassType vueComponent, @Mock JField test)
    {
        String html = "<div v-bind:title=\"test\"></div>";

        when(test.getName()).thenReturn("test");
        when(vueComponent.getFields()).thenReturn(new JField[] { test });

        TemplateParserResult result = templateParser.parseHtmlTemplate(html, vueComponent);

        assertEquals("<div v-bind:title=\"" + EXPRESSION_PREFIX + "0\"></div>", pr(result));
        assertEquals(1, result.getTemplateExpressions().size());
        assertEquals("test", result.getTemplateExpressions().get(EXPRESSION_PREFIX + "0"));
    }

    @Test
    void supportAtSign(@Mock JClassType vueComponent, @Mock JField test)
    {
        String html = "<div @class=\"test\"></div>";

        when(test.getName()).thenReturn("test");
        when(vueComponent.getFields()).thenReturn(new JField[] { test });

        TemplateParserResult result = templateParser.parseHtmlTemplate(html, vueComponent);

        assertEquals("<div @class=\"" + EXPRESSION_PREFIX + "0\"></div>", pr(result));
    }

    @Test
    void supportTwoDots(@Mock JClassType vueComponent, @Mock JField test)
    {
        String html = "<div :title=\"test\"></div>";

        when(test.getName()).thenReturn("test");
        when(vueComponent.getFields()).thenReturn(new JField[] { test });

        TemplateParserResult result = templateParser.parseHtmlTemplate(html, vueComponent);

        assertEquals("<div :title=\"" + EXPRESSION_PREFIX + "0\"></div>", pr(result));
    }

    @Test
    void supportMustache(@Mock JClassType vueComponent, @Mock JField test)
    {
        String html = "<div>{{ test }}</div>";

        when(test.getName()).thenReturn("test");
        when(vueComponent.getFields()).thenReturn(new JField[] { test });

        TemplateParserResult result = templateParser.parseHtmlTemplate(html, vueComponent);

        assertEquals("<div>{{ " + EXPRESSION_PREFIX + "0 }}</div>", pr(result));
        assertEquals(1, result.getTemplateExpressions().size());
        assertEquals("test", result.getTemplateExpressions().get(EXPRESSION_PREFIX + "0"));
    }

    @Test
    void supportMustacheMultiple(@Mock JClassType vueComponent, @Mock JField test,
        @Mock JField test2)
    {
        String html = "<div>{{ test }} {{ test2 }}</div>";

        when(test.getName()).thenReturn("test");
        when(test2.getName()).thenReturn("test2");
        when(vueComponent.getFields()).thenReturn(new JField[] { test, test2 });

        TemplateParserResult result = templateParser.parseHtmlTemplate(html, vueComponent);

        assertEquals("<div>{{ " + EXPRESSION_PREFIX + "0 }} {{ " + EXPRESSION_PREFIX + "1 }}</div>",
            pr(result)
        );
        assertEquals(2, result.getTemplateExpressions().size());
        assertEquals("test", result.getTemplateExpressions().get(EXPRESSION_PREFIX + "0"));
        assertEquals("test2", result.getTemplateExpressions().get(EXPRESSION_PREFIX + "1"));
    }

    @Test
    void testChangeExpression(@Mock JClassType vueComponent, @Mock JField myObject)
    {
        String html = "<div>{{ myObject.hello() }}</div>";

        when(myObject.getName()).thenReturn("myObject");
        when(vueComponent.getFields()).thenReturn(new JField[] { myObject });

        TemplateParserResult result = templateParser.parseHtmlTemplate(html, vueComponent);

        assertEquals("<div>{{ " + EXPRESSION_PREFIX + "0 }}</div>", pr(result));
        assertEquals(1, result.getTemplateExpressions().size());
        assertEquals(
            "myObject.hello()", result.getTemplateExpressions().get(EXPRESSION_PREFIX + "0"));
    }

    private String pr(TemplateParserResult result)
    {
        return result.getTemplateWithReplacements().replaceAll("\n *", "");
    }
}
