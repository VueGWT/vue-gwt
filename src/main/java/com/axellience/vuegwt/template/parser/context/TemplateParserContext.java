package com.axellience.vuegwt.template.parser.context;

import com.axellience.vuegwt.template.parser.InvalidExpressionException;
import com.google.gwt.core.ext.typeinfo.*;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Adrien Baron
 */
public class TemplateParserContext
{
    public static final String CONTEXT_PREFIX = "VUE_GWT_CTX_";
    private final Deque<ContextLayer> contextLayers = new ArrayDeque<>();
    private final JClassType vueComponentClass;
    private int contextId = 0;

    public TemplateParserContext(JClassType vueComponentClass)
    {
        this.vueComponentClass = vueComponentClass;
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

    public void addContextLayer()
    {
        contextLayers.push(new ContextLayer(CONTEXT_PREFIX + contextId + "_"));
        contextId++;
    }

    public ContextLayer popContextLayer()
    {
        return contextLayers.pop();
    }

    public boolean isInContextLayer()
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

    public JClassType getVueComponentClass()
    {
        return vueComponentClass;
    }
}
