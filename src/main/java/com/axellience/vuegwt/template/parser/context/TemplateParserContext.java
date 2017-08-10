package com.axellience.vuegwt.template.parser.context;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.template.TemplateResource;
import com.axellience.vuegwt.client.jsnative.jstypes.JsArray;
import com.axellience.vuegwt.template.parser.variable.LocalVariableInfo;
import com.axellience.vuegwt.template.parser.variable.VariableInfo;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.dom.client.NativeEvent;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import org.jsoup.nodes.Node;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Context of the parser.
 * This holds information about imports and variable that exist in the Component.
 * It also holds information about the current node being processed.
 * @author Adrien Baron
 */
public class TemplateParserContext
{
    private final JClassType componentWithTemplateClass;
    private final ContextLayer rootContext;
    private final Deque<ContextLayer> contextLayers = new ArrayDeque<>();

    private Map<String, String> classNameToFullyQualifiedName = new HashMap<>();

    private Node currentNode;

    /**
     * Build the context based on a given {@link TemplateResource} Class.
     * @param componentWithTemplateClass The {@link TemplateResource} we are parsing the
     * template of
     */
    public TemplateParserContext(JClassType componentWithTemplateClass)
    {
        this.componentWithTemplateClass = componentWithTemplateClass;

        this.addImport(NativeEvent.class.getCanonicalName());
        this.addImport(JsArray.class.getCanonicalName());

        this.rootContext = new ContextLayer();
        this.rootContext.addVariable(String.class, "_uid");
        registerFieldsInContext(componentWithTemplateClass);

        this.contextLayers.add(this.rootContext);
    }

    /**
     * Process the {@link TemplateResource} class to extract all the fields that will be
     * visible in the template.
     * It is called recursively to find parent fields in case of inheritance.
     * @param vueComponentClass The class to process
     */
    private void registerFieldsInContext(JClassType vueComponentClass)
    {
        if (vueComponentClass == null || vueComponentClass
            .getQualifiedSourceName()
            .equals(VueComponent.class.getCanonicalName()))
            return;

        // Add all visible fields to the context
        boolean hasJsType = vueComponentClass.getAnnotation(JsType.class) != null;
        Arrays
            .stream(vueComponentClass.getFields())
            .filter(jField -> (hasJsType && jField.isPublic())
                || jField.getAnnotation(JsProperty.class) != null)
            .forEach(this.rootContext::addVariable);

        registerFieldsInContext(vueComponentClass.getSuperclass());
    }

    /**
     * Add a variable to the root context
     * @param type The type of the variable to add
     * @param name The name of the variable to add
     */
    public void addRootVariable(String type, String name)
    {
        this.rootContext.addVariable(type, name);
    }

    /**
     * Add a context layer. Used when entering a node with v-for.
     */
    public void addContextLayer()
    {
        contextLayers.push(new ContextLayer());
    }

    /**
     * Pop a context layer. Used when leaving a node with v-for.
     */
    public void popContextLayer()
    {
        contextLayers.pop();
    }

    /**
     * Add a local variable in the current context.
     * @param typeQualifiedName The type of the variable
     * @param name The name of the variable
     * @return {@link LocalVariableInfo} for the added variable
     */
    public LocalVariableInfo addLocalVariable(String typeQualifiedName, String name)
    {
        return contextLayers.getFirst().addLocalVariable(typeQualifiedName, name);
    }

    /**
     * Find a variable in the context stack.
     * @param name Name of the variable to get
     * @return Information about the variable
     */
    public VariableInfo findVariable(String name)
    {
        for (ContextLayer contextLayer : contextLayers)
        {
            VariableInfo variableInfo = contextLayer.getVariableInfo(name);
            if (variableInfo != null)
                return variableInfo;
        }

        return null;
    }

    /**
     * Add a Java Import to the context.
     * @param fullyQualifiedName The fully qualified name of the class to import
     */
    public void addImport(String fullyQualifiedName)
    {
        String[] importSplit = fullyQualifiedName.split("\\.");
        String className = importSplit[importSplit.length - 1];

        classNameToFullyQualifiedName.put(className, fullyQualifiedName);
    }

    /**
     * Return the fully qualified name for a given class. Only works if the class has been imported.
     * @param className The name of the class to get the fully qualified name of
     * @return The fully qualified name, or the className if it's unknown
     */
    public String getFullyQualifiedNameForClassName(String className)
    {
        if (!classNameToFullyQualifiedName.containsKey(className))
            return className;

        return classNameToFullyQualifiedName.get(className);
    }

    /**
     * Return true if we have an import for the given className
     * @param className The className we want to check
     * @return True if we have an import, false otherwise
     */
    public boolean hasImport(String className)
    {
        return classNameToFullyQualifiedName.containsKey(className);
    }

    /**
     * Return the current HTML {@link Node} being processed
     * @return The current HTML {@link Node}
     */
    public Node getCurrentNode()
    {
        return currentNode;
    }

    /**
     * Set the current HTML {@link Node} being processed
     * @param currentNode The current HTML {@link Node}
     */
    public void setCurrentNode(Node currentNode)
    {
        this.currentNode = currentNode;
    }

    /**
     * Simple getter for the currently processed VueComponentClass.
     * Used for debugging.
     * @return The currently process VueComponent class
     */
    public JClassType getComponentWithTemplateClass()
    {
        return componentWithTemplateClass;
    }
}
