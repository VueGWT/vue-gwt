package com.axellience.vuegwt.template.parser;

import com.axellience.vuegwt.template.parser.context.LocalVariableInfo;
import com.axellience.vuegwt.template.parser.context.TemplateParserContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Adrien Baron
 */
public class VForDefinition
{
    private static Pattern VFOR_VARIABLE = Pattern.compile("([^ ]*) ([^ ]*)");
    private static Pattern VFOR_VARIABLE_AND_INDEX =
        Pattern.compile("\\(([^ ]*) ([^,]*),([^\\)]*)\\)");

    private final String inExpression;
    private LocalVariableInfo loopVariableInfo = null;
    private LocalVariableInfo indexVariableInfo = null;

    public VForDefinition(String vForValue, TemplateParserContext context)
    {
        String[] splitExpression = splitVForExpression(vForValue);

        String loopVariablesDefinition = splitExpression[0].trim();
        inExpression = splitExpression[1].trim();

        Matcher matcher = VFOR_VARIABLE.matcher(loopVariablesDefinition);
        if (matcher.matches())
        {
            initLoopVariable(matcher.group(1), matcher.group(2), context);
            indexVariableInfo = null;
            return;
        }

        matcher = VFOR_VARIABLE_AND_INDEX.matcher(loopVariablesDefinition);
        if (matcher.matches())
        {
            initLoopVariable(matcher.group(1), matcher.group(2), context);
            initIndexVariable(matcher.group(3), context);
            return;
        }

        throw new InvalidExpressionException("Invalid v-for found: " + vForValue);
    }

    private void initLoopVariable(String type, String name, TemplateParserContext context)
    {
        this.loopVariableInfo =
            context.addLocalVariable(context.getFullyQualifiedNameForClassName(type.trim()), name.trim());
    }

    private void initIndexVariable(String name, TemplateParserContext context)
    {
        this.indexVariableInfo = context.addLocalVariable("int", name.trim());
    }

    /**
     * Split the value of a v-for into 2 blocks (before/after the " in " or " of ")
     * @param vForValue The value of the v-for attribute
     * @return The two
     */
    private String[] splitVForExpression(String vForValue)
    {
        String[] splitExpression = vForValue.split(" in ");
        if (splitExpression.length != 2)
            splitExpression = vForValue.split(" of ");

        if (splitExpression.length != 2)
            throw new InvalidExpressionException("Invalid v-for found: " + vForValue);

        return splitExpression;
    }

    public LocalVariableInfo getLoopVariableInfo()
    {
        return loopVariableInfo;
    }

    public LocalVariableInfo getIndexVariableInfo()
    {
        return indexVariableInfo;
    }

    public String getInExpression()
    {
        return inExpression;
    }
}
