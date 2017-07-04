package com.axellience.vuegwt.template.parser.context;

import com.axellience.vuegwt.client.gwtextension.TemplateExpressionKind;
import com.axellience.vuegwt.jsr69.component.annotations.Computed;
import com.axellience.vuegwt.template.parser.InvalidExpressionException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JMethod;
import org.jsoup.nodes.Node;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Adrien Baron
 */
public class TemplateParserContext
{
    public static final String CONTEXT_PREFIX = "VUE_GWT_CTX_";
    private final ContextLayer rootContext;
    private final Deque<ContextLayer> contextLayers = new ArrayDeque<>();
    private final JClassType vueComponentClass;
    private int contextId = 0;

    private Map<String, String> classNameToFullyQualifiedName = new HashMap<>();

    private Node currentNode;
    private String currentExpressionReturnType;
    private TemplateExpressionKind currentExpressionKind;

    public TemplateParserContext(JClassType vueComponentClass)
    {
        this.vueComponentClass = vueComponentClass;
        this.rootContext = new ContextLayer("");

        this.rootContext.addVariable(String.class, "_uid");
        for (JField jField : vueComponentClass.getFields())
        {
            this.rootContext.addVariable(jField);
        }
        for (JMethod jMethod : vueComponentClass.getMethods())
        {
            Computed computed = jMethod.getAnnotation(Computed.class);
            if (computed == null)
                continue;

            String name = computed.propertyName();
            if ("".equals(name))
                name = jMethod.getName();

            this.rootContext.addComputedVariable(jMethod.getReturnType(), name);
        }

        this.contextLayers.add(this.rootContext);
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

    public LocalVariableInfo addLocalVariable(String typeQualifiedName, String name)
    {
        return contextLayers.getFirst().addLocalVariable(typeQualifiedName, name);
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

    public void addImport(String fullyQualifiedName)
    {
        String[] importSplit = fullyQualifiedName.split("\\.");
        String className = importSplit[importSplit.length - 1];

        classNameToFullyQualifiedName.put(className, fullyQualifiedName);
    }

    public String getFullyQualifiedNameForClassName(String className)
    {
        if (!classNameToFullyQualifiedName.containsKey(className))
            return className;

        return classNameToFullyQualifiedName.get(className);
    }

    public void addRootVariable(String type, String name)
    {
        this.rootContext.addVariable(type, name);
    }
}
