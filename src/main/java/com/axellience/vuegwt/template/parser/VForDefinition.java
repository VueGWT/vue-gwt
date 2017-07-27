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
    private static Pattern VFOR_VARIABLE_AND_KEY =
        Pattern.compile("\\(([^ ]*) ([^,]*),([^\\)]*)\\)");
    private static Pattern VFOR_VARIABLE_AND_KEY_AND_INDEX =
        Pattern.compile("\\(([^ ]*) ([^,]*),([^,]*),([^\\)]*)\\)");

    private final String inExpression;
    private String inExpressionType;
    private LocalVariableInfo loopVariableInfo = null;
    private LocalVariableInfo keyVariableInfo = null;
    private LocalVariableInfo indexVariableInfo = null;

    public VForDefinition(String vForValue, TemplateParserContext context)
    {
        String[] splitExpression = splitVForExpression(vForValue);

        String loopVariablesDefinition = splitExpression[0].trim();
        inExpression = splitExpression[1].trim();

        if (vForOnRange(inExpression, loopVariablesDefinition, context))
            return;

        boolean iterateOnArray = !inExpression.startsWith("(Object)");

        if (iterateOnArray)
        {
            inExpressionType = "JsArray";

            if (vForVariableAndIndex(loopVariablesDefinition, context))
                return;
            if (vForVariable(loopVariablesDefinition, context))
                return;
        }
        else
        {
            inExpressionType = "Object";

            if (vForVariableAndKeyAndIndex(loopVariablesDefinition, context))
                return;
            if (vForVariableAndKey(loopVariablesDefinition, context))
                return;
            if (vForVariable(loopVariablesDefinition, context))
                return;
        }

        throw new InvalidExpressionException("Invalid v-for found: " + vForValue);
    }

    /**
     * v-for on an array with just a loop variable:
     * "Item item in myArray"
     * @param loopVariablesDefinition The variable definition ("Item item" above)
     * @param context The context of the parser
     * @return true if we managed the case, false otherwise
     */
    private boolean vForVariable(String loopVariablesDefinition,
        TemplateParserContext context)
    {
        Matcher matcher = VFOR_VARIABLE.matcher(loopVariablesDefinition);
        if (matcher.matches())
        {
            initLoopVariable(matcher.group(1), matcher.group(2), context);
            indexVariableInfo = null;
            return true;
        }

        return false;
    }

    /**
     * v-for on an array with just a loop variable and an index:
     * "(Item item, index) in myArray"
     * @param loopVariablesDefinition The variable definition ("(Item item, index)" above)
     * @param context The context of the parser
     * @return true if we managed the case, false otherwise
     */
    private boolean vForVariableAndIndex(String loopVariablesDefinition,
        TemplateParserContext context)
    {
        Matcher matcher = VFOR_VARIABLE_AND_INDEX.matcher(loopVariablesDefinition);
        if (matcher.matches())
        {
            initLoopVariable(matcher.group(1), matcher.group(2), context);
            initIndexVariable(matcher.group(3), context);
            return true;
        }

        return false;
    }

    /**
     * v-for on an Object with a loop variable and the key:
     * "(Item item, key) in (Object) myObject"
     * @param loopVariablesDefinition The variable definition ("(Item item, key)" above)
     * @param context The context of the parser
     * @return true if we managed the case, false otherwise
     */
    private boolean vForVariableAndKey(String loopVariablesDefinition,
        TemplateParserContext context)
    {
        Matcher matcher = VFOR_VARIABLE_AND_KEY.matcher(loopVariablesDefinition);
        if (matcher.matches())
        {
            initLoopVariable(matcher.group(1), matcher.group(2), context);
            initKeyVariable(matcher.group(3), context);
            return true;
        }

        return false;
    }

    /**
     * v-for on an Object with a loop variable, the key and the index:
     * "(Item item, key, index) in (Object) myObject"
     * @param loopVariablesDefinition The variable definition ("(Item item, key, index)" above)
     * @param context The context of the parser
     * @return true if we managed the case, false otherwise
     */
    private boolean vForVariableAndKeyAndIndex(String loopVariablesDefinition,
        TemplateParserContext context)
    {
        Matcher matcher = VFOR_VARIABLE_AND_KEY_AND_INDEX.matcher(loopVariablesDefinition);
        if (matcher.matches())
        {
            initLoopVariable(matcher.group(1), matcher.group(2), context);
            initKeyVariable(matcher.group(3), context);
            initIndexVariable(matcher.group(4), context);
            return true;
        }

        return false;
    }

    /**
     * v-for on an a range
     * "n in 5"
     * @param inExpression The expression after "in" ("5" in the example above)
     * @param loopVariableDefinition The variable definition ("n" above)
     * @param context The context of the parser
     * @return true if we managed the case, false otherwise
     */
    private boolean vForOnRange(String inExpression, String loopVariableDefinition,
        TemplateParserContext context)
    {
        // Try to parse the inExpression as an Int
        try {
            Integer.parseInt(inExpression);
            this.initLoopVariable("int", loopVariableDefinition, context);
            inExpressionType = "int";
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Init the loop variable and add it to the parser context
     * @param type Java type of the variable, will look for qualified class name in the context
     * @param name Name of the variable
     * @param context Context of the template parser
     */
    private void initLoopVariable(String type, String name, TemplateParserContext context)
    {
        this.loopVariableInfo =
            context.addLocalVariable(context.getFullyQualifiedNameForClassName(type.trim()),
                name.trim());
    }

    /**
     * Init the index variable and add it to the parser context
     * @param name Name of the variable
     * @param context Context of the template parser
     */
    private void initIndexVariable(String name, TemplateParserContext context)
    {
        this.indexVariableInfo = context.addLocalVariable("int", name.trim());
    }

    /**
     * Init the key variable and add it to the parser context
     * @param name Name of the variable
     * @param context Context of the template parser
     */
    private void initKeyVariable(String name, TemplateParserContext context)
    {
        this.keyVariableInfo = context.addLocalVariable("String", name.trim());
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

    public String getInExpression()
    {
        if ("JsArray".equals(inExpressionType))
            return "JsArray.from(" + inExpression + ")";

        return inExpression;
    }

    public String getInExpressionType()
    {
        return inExpressionType;
    }

    public String getVariableDefinition()
    {
        String variableDefinition = loopVariableInfo.getGlobalName();

        if (keyVariableInfo != null || indexVariableInfo != null)
        {
            variableDefinition = "(" + variableDefinition;

            if (keyVariableInfo != null)
                variableDefinition += ", " + keyVariableInfo.getGlobalName();

            if (indexVariableInfo != null)
                variableDefinition += ", " + indexVariableInfo.getGlobalName();

            variableDefinition += ")";
        }

        return variableDefinition;
    }

    public boolean isInExpressionJava()
    {
        return !"int".equals(inExpressionType);
    }
}
