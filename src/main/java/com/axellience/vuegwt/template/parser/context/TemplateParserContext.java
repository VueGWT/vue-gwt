package com.axellience.vuegwt.template.parser.context;

import com.axellience.vuegwt.client.gwtextension.TemplateExpressionKind;
import com.axellience.vuegwt.template.parser.InvalidExpressionException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JType;
import org.jsoup.nodes.Node;

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

    private Node currentNode;
    private String currentExpressionReturnType;
    private TemplateExpressionKind currentExpressionKind;

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

    public Node getCurrentNode()
    {
        return currentNode;
    }

    public void setCurrentNode(Node currentNode)
    {
        this.currentNode = currentNode;
    }

    public String getCurrentExpressionReturnType()
    {
        return currentExpressionReturnType;
    }

    public void setCurrentExpressionReturnType(String currentExpressionReturnType)
    {
        this.currentExpressionReturnType = currentExpressionReturnType;
    }

    public TemplateExpressionKind getCurrentExpressionKind()
    {
        return currentExpressionKind;
    }

    public void setCurrentExpressionKind(TemplateExpressionKind currentExpressionKind)
    {
        this.currentExpressionKind = currentExpressionKind;
    }
}
