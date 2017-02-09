package com.axellience.vuegwt.template;

import com.axellience.mockito.MockitoExtension;
import com.google.gwt.core.ext.typeinfo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static com.axellience.vuegwt.client.gwtextension.TemplateResource.COLLECTION_ARRAY_SUFFIX;
import static com.axellience.vuegwt.client.gwtextension.TemplateResource.COLLECTION_PREFIX;
import static com.axellience.vuegwt.client.gwtextension.TemplateResource.EXPRESSION_PREFIX;
import static com.axellience.vuegwt.template.TemplateParserContext.CONTEXT_PREFIX;
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

    @Test
    void testFor(@Mock JClassType vueComponent, @Mock JField todos, @Mock JClassType todosType,
        @Mock JParameterizedType parameterizedType, @Mock JClassType todoType,
        @Mock TypeOracle typeOracle, @Mock JClassType iterableType) throws NotFoundException
    {
        String html = "<div v-for=\"todo in todos\">{{ todo.getName() }}</div>";

        when(todos.getName()).thenReturn("todos");
        when(todos.getType()).thenReturn(todosType);
        when(todosType.isParameterized()).thenReturn(parameterizedType);
        when(parameterizedType.getTypeArgs()).thenReturn(new JClassType[] { todoType });
        when(vueComponent.getFields()).thenReturn(new JField[] { todos });
        when(parameterizedType.getOracle()).thenReturn(typeOracle);
        when(typeOracle.getType(Iterable.class.getName())).thenReturn(parameterizedType);
        when(parameterizedType.getImplementedInterfaces()).thenReturn(
            new JClassType[] { parameterizedType });

        TemplateParserResult result = templateParser.parseHtmlTemplate(html, vueComponent);

        assertEquals("<div v-for=\"" + CONTEXT_PREFIX + "0_todo in " + COLLECTION_PREFIX + "0" +
            COLLECTION_ARRAY_SUFFIX + "\">{{ " + EXPRESSION_PREFIX + "0 }}</div>", pr(result));

        assertEquals(1, result.getTemplateExpressions().size());
        assertEquals(CONTEXT_PREFIX + "0_todo.getName()",
            result.getTemplateExpressions().get(EXPRESSION_PREFIX + "0")
        );

        assertEquals(1, result.getCollectionsExpressions().size());
        assertEquals("todos", result.getCollectionsExpressions().get(COLLECTION_PREFIX + "0"));
    }

    private String pr(TemplateParserResult result)
    {
        return result.getTemplateWithReplacements().replaceAll("\n *", "");
    }
}
