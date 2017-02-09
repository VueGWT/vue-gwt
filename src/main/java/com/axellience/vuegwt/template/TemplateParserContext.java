package com.axellience.vuegwt.template;

import com.google.gwt.core.ext.typeinfo.*;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Adrien Baron
 */
public class TemplateParserContext
{
    public static final String              CONTEXT_PREFIX = "VUE_GWT_CTX_";
    private final       Deque<ContextLayer> contextLayers  = new ArrayDeque<>();
    private             int                 contextNumber  = 0;

    public TemplateParserContext(JClassType vueComponentClass)
    {
        ContextLayer root = new ContextLayer("");
        contextLayers.add(root);
        for (JField jField : vueComponentClass.getFields())
        {
            root.addRootVariable(jField);
        }
    }

    public VariableInfo addLocalVariable(JType type, String name)
    {
        return contextLayers.getFirst().addLocalVariable(type, name);
    }

    public void addContext()
    {
        contextLayers.push(new ContextLayer(CONTEXT_PREFIX + contextNumber + "_"));
        contextNumber++;
    }

    public ContextLayer popContext()
    {
        return contextLayers.pop();
    }

    public boolean isInContext()
    {
        return contextLayers.size() > 1;
    }

    public VariableInfo findVariableInContext(String name)
    {
        for (ContextLayer contextLayer : contextLayers)
        {
            if (contextLayer.hasVariable(name))
                return contextLayer.getVariableInfo(name);
        }

        throw new InvalidExpressionException("Couldn't find the type: " + name);
    }
}
