package com.axellience.vuegwt.processors.component.template.parser.context;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.lang.model.element.TypeElement;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.tools.JsUtils;
import com.axellience.vuegwt.core.client.tools.VForExpressionUtil;
import com.axellience.vuegwt.processors.component.template.parser.context.localcomponents.LocalComponent;
import com.axellience.vuegwt.processors.component.template.parser.context.localcomponents.LocalComponents;
import com.axellience.vuegwt.processors.component.template.parser.variable.LocalVariableInfo;
import com.axellience.vuegwt.processors.component.template.parser.variable.VariableInfo;
import com.squareup.javapoet.TypeName;

import elemental2.dom.Event;
import net.htmlparser.jericho.Segment;

/**
 * Context of the parser.
 * This holds information about imports and variable that exist in the Component.
 * It also holds information about the current node being processed.
 * @author Adrien Baron
 */
public class TemplateParserContext
{
    private final TypeElement componentTypeElement;
    private final LocalComponents localComponents;
    private final ContextLayer rootLayer;
    private final Deque<ContextLayer> contextLayers = new ArrayDeque<>();

    // For import
    private Map<String, String> classNameToFullyQualifiedName = new HashMap<>();
    // For static imports
    private Map<String, String> methodNameToFullyQualifiedName = new HashMap<>();

    private Segment currentSegment;

    /** In some cases mandatory attributes must be added to each element during template parsing, for example to support scoped styles */
    private final Map<String, String> mandatoryAttributes = new HashMap<>();

    /**
     * Build the context based on a given {@link IsVueComponent} Class.
     * @param componentTypeElement The {@link IsVueComponent} class we process in this context
     * @param localComponents Components registered locally, used to check property bindings
     */
    public TemplateParserContext(TypeElement componentTypeElement, LocalComponents localComponents)
    {
        this.componentTypeElement = componentTypeElement;
        this.localComponents = localComponents;
        this.addImport(Event.class.getCanonicalName());
        this.addImport(Math.class.getCanonicalName());
        this.addImport(JsUtils.class.getCanonicalName());
        this.addImport(VForExpressionUtil.class.getCanonicalName());
        this.addStaticImport(JsUtils.class.getCanonicalName() + ".map");
        this.addStaticImport(JsUtils.class.getCanonicalName() + ".e");
        this.addStaticImport(JsUtils.class.getCanonicalName() + ".array");

        this.rootLayer = new ContextLayer();
        this.rootLayer.addMethod("vue");

        this.contextLayers.add(this.rootLayer);
    }

    /**
     * Add a variable to the root context.
     * @param type The type of the variable to add
     * @param name The name of the variable to add
     */
    public void addRootVariable(TypeName type, String name)
    {
        this.rootLayer.addVariable(type, name);
    }

    /**
     * Add a variable to the root context.
     * @param type The type of the variable to add
     * @param name The name of the variable to add
     */
    public void addRootVariable(String type, String name)
    {
        this.rootLayer.addVariable(type, name);
    }

    /**
     * Register a method in the root context
     * @param methodName The name of the method
     */
    public void addRootMethod(String methodName)
    {
        this.rootLayer.addMethod(methodName);
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
     * Search if the method with the given name exist in the context stack.
     * This will allow to catch basic error at the parser level, also this allow us to know
     * when a method is used in a template expression and use a method call instead of a computed
     * property.
     * We only look in the the root context, because methods can't be declared on the fly in the
     * template, so they can only exist in the root context.
     * This doesn't check that parameters from the call match, we leave this to the Java compiler.
     * @param name The name of the method to look for
     * @return True if it exists, false otherwise
     */
    public boolean hasMethod(String name)
    {
        return rootLayer.hasMethod(name);
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
     * Add a Java Static Import to the context.
     * @param fullyQualifiedName The fully qualified name of the method to import
     */
    public void addStaticImport(String fullyQualifiedName)
    {
        String[] importSplit = fullyQualifiedName.split("\\.");
        String methodName = importSplit[importSplit.length - 1];

        methodNameToFullyQualifiedName.put(methodName, fullyQualifiedName);
    }

    /**
     * Return the fully qualified name for a given method. Only works if the method has been
     * statically imported.
     * @param methodName The name of the method to get the fully qualified name of
     * @return The fully qualified name, or the method name if it's unknown
     */
    public String getFullyQualifiedNameForMethodName(String methodName)
    {
        if (!methodNameToFullyQualifiedName.containsKey(methodName))
            return methodName;

        return methodNameToFullyQualifiedName.get(methodName);
    }

    /**
     * Return true if we have a static import for the given methodName
     * @param methodName The methodName we want to check
     * @return True if we have an import, false otherwise
     */
    public boolean hasStaticMethod(String methodName)
    {
        return methodNameToFullyQualifiedName.containsKey(methodName)
            || methodNameToFullyQualifiedName.containsValue(methodName);
    }

    /**
     * Return the number of the line currently processed in the HTML
     * @return The number of the current line being processed or empty
     */
    public Optional<Integer> getCurrentLine()
    {
        if (currentSegment == null)
            return Optional.empty();

        return Optional.of(currentSegment.getSource().getRow(currentSegment.getBegin()));
    }

    /**
     * Set the current HTML {@link Segment} being processed.
     * Used for error message and comment on expressions
     * @param currentSegment The current HTML {@link Segment}
     */
    public void setCurrentSegment(Segment currentSegment)
    {
        this.currentSegment = currentSegment;
    }

    /**
     * Simple getter for the currently processed {@link IsVueComponent} Template name.
     * Used for debugging.
     * @return The currently process {@link IsVueComponent} Template name
     */
    public String getTemplateName()
    {
        return componentTypeElement.getSimpleName().toString() + ".html";
    }

    public Optional<LocalComponent> getLocalComponent(String tagName)
    {
        return localComponents.getLocalComponent(tagName);
    }

    public TypeElement getComponentTypeElement()
    {
        return componentTypeElement;
    }

    public Map<String, String> getMandatoryAttributes() {
        return mandatoryAttributes;
    }

}
