package com.axellience.vuegwt.jsr69.component.constructor.options;

import com.axellience.vuegwt.client.VueGWT;
import com.axellience.vuegwt.client.component.HasRender;
import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.hooks.HasActivated;
import com.axellience.vuegwt.client.component.hooks.HasBeforeCreate;
import com.axellience.vuegwt.client.component.hooks.HasBeforeDestroy;
import com.axellience.vuegwt.client.component.hooks.HasBeforeMount;
import com.axellience.vuegwt.client.component.hooks.HasBeforeUpdate;
import com.axellience.vuegwt.client.component.hooks.HasCreated;
import com.axellience.vuegwt.client.component.hooks.HasDeactivated;
import com.axellience.vuegwt.client.component.hooks.HasDestroyed;
import com.axellience.vuegwt.client.component.hooks.HasMounted;
import com.axellience.vuegwt.client.component.hooks.HasUpdated;
import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.client.component.options.computed.ComputedKind;
import com.axellience.vuegwt.client.jsnative.jstypes.JsArray;
import com.axellience.vuegwt.client.tools.JsTools;
import com.axellience.vuegwt.client.vnode.builder.CreateElementFunction;
import com.axellience.vuegwt.client.vnode.builder.VNodeBuilder;
import com.axellience.vuegwt.jsr69.GenerationUtil;
import com.axellience.vuegwt.jsr69.component.TemplateProviderGenerator;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.Computed;
import com.axellience.vuegwt.jsr69.component.annotations.Prop;
import com.axellience.vuegwt.jsr69.component.annotations.PropValidator;
import com.axellience.vuegwt.jsr69.component.annotations.Watch;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Generate VueComponentOptions from the user Java Components
 * @author Adrien Baron
 */
public class VueComponentOptionsGenerator
{
    public static String COMPONENT_OPTIONS_SUFFIX = "Options";

    private static Map<String, Class> HOOKS_MAP = new HashMap<>();

    static
    {
        // Init the map of lifecycle hooks for fast type type check
        HOOKS_MAP.put("beforeCreate", HasBeforeCreate.class);
        HOOKS_MAP.put("created", HasCreated.class);
        HOOKS_MAP.put("beforeMount", HasBeforeMount.class);
        HOOKS_MAP.put("mounted", HasMounted.class);
        HOOKS_MAP.put("beforeUpdate", HasBeforeUpdate.class);
        HOOKS_MAP.put("updated", HasUpdated.class);
        HOOKS_MAP.put("activated", HasActivated.class);
        HOOKS_MAP.put("deactivated", HasDeactivated.class);
        HOOKS_MAP.put("beforeDestroy", HasBeforeDestroy.class);
        HOOKS_MAP.put("destroyed", HasDestroyed.class);
    }

    private final ProcessingEnvironment processingEnv;
    private final Elements elementsUtils;
    private boolean hasComponentJsTypeAnnotation;

    public VueComponentOptionsGenerator(ProcessingEnvironment processingEnv)
    {
        this.processingEnv = processingEnv;
        this.elementsUtils = processingEnv.getElementUtils();
    }

    /**
     * Generate an {@link VueComponentOptions} for our Vue Component.
     * @param componentTypeElement The {@link VueComponent} class to generate {@link
     * VueComponentOptions} from
     * @return The generated TypeSpec for our {@link VueComponentOptions} class
     */
    public TypeSpec generate(TypeElement componentTypeElement)
    {
        String packageName =
            elementsUtils.getPackageOf(componentTypeElement).getQualifiedName().toString();
        String className = componentTypeElement.getSimpleName().toString();
        String generatedClassName = className + COMPONENT_OPTIONS_SUFFIX;
        hasComponentJsTypeAnnotation = componentTypeElement.getAnnotation(JsType.class) != null;

        Component annotation = componentTypeElement.getAnnotation(Component.class);

        Builder optionsClassBuilder =
            createOptionsClassBuilder(componentTypeElement, generatedClassName);

        // Initialize constructor
        MethodSpec.Builder constructorBuilder =
            MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);

        // Add the Java Component Instance initialization
        constructorBuilder.addStatement(
            "this.setVueComponentJsTypeConstructor($T.getJavaConstructor($T.class))",
            VueGWT.class,
            TypeName.get(componentTypeElement.asType()));

        // Set the name of the component
        if (!"".equals(annotation.name()))
            constructorBuilder.addStatement("this.setName($S)", annotation.name());

        // Add template initialization
        if (annotation.hasTemplate())
        {
            constructorBuilder.addStatement("this.setTemplateResource($T.INSTANCE.$L())",
                ClassName.get(packageName,
                    className + TemplateProviderGenerator.TEMPLATE_BUNDLE_SUFFIX),
                TemplateProviderGenerator.TEMPLATE_BUNDLE_METHOD_NAME);
        }

        // Data and props
        processDataProperties(componentTypeElement, annotation, constructorBuilder);

        // Methods
        processMethods(componentTypeElement, optionsClassBuilder, constructorBuilder);

        // Finish building the constructor
        optionsClassBuilder.addMethod(constructorBuilder.build());

        return optionsClassBuilder.build();
    }

    /**
     * Create a builder for our Option class.
     * @param componentTypeElement The Component class we are processing
     * @param generatedTypeName The name our Options class should have
     * @return A class builder for our Options class
     */
    private Builder createOptionsClassBuilder(TypeElement componentTypeElement,
        String generatedTypeName)
    {
        return TypeSpec
            .classBuilder(generatedTypeName)
            .addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
            .superclass(ParameterizedTypeName.get(ClassName.get(VueComponentOptions.class),
                ClassName.get(componentTypeElement)));
    }

    /**
     * Process properties from the Component Class.
     * @param componentTypeElement Component to process
     * @param annotation The Component annotation
     * @param constructorBuilder The builder for the Constructor method of our Option class
     */
    private void processDataProperties(TypeElement componentTypeElement, Component annotation,
        MethodSpec.Builder constructorBuilder)
    {
        constructorBuilder.addStatement("$T<$T> propertiesName = new $T<>()",
            List.class,
            String.class,
            LinkedList.class);
        ElementFilter
            .fieldsIn(componentTypeElement.getEnclosedElements())
            .forEach(variableElement -> {
                String javaName = variableElement.getSimpleName().toString();
                Prop prop = variableElement.getAnnotation(Prop.class);

                if (prop != null)
                {
                    if (!isVisibleInJS(variableElement))
                        throw new RuntimeException("@Prop "
                            + javaName
                            + " must also have @JsProperty annotation in VueComponent "
                            + componentTypeElement.getQualifiedName().toString()
                            + ".");

                    constructorBuilder.addStatement("this.addJavaProp($S, $L, $S)",
                        javaName,
                        prop.required(),
                        prop.checkType() ? getNativeNameForJavaType(variableElement.asType()) :
                            null);
                }
                else
                {
                    // Ignore properties not visible in JS
                    if (!isVisibleInJS(variableElement))
                        return;

                    constructorBuilder.addStatement("propertiesName.add($S)", javaName);
                }
            });
        constructorBuilder.addStatement("this.initData(propertiesName, $L)",
            annotation.useFactory());
    }

    /**
     * Process methods from the Component Class.
     * @param componentTypeElement Component to process
     * @param optionsClassBuilder The builder for our Option class
     * @param constructorBuilder The builder for the Constructor method of our Option class
     */
    private void processMethods(TypeElement componentTypeElement, Builder optionsClassBuilder,
        MethodSpec.Builder constructorBuilder)
    {
        ElementFilter
            .methodsIn(componentTypeElement.getEnclosedElements())
            .forEach(executableElement -> {
                String javaName = executableElement.getSimpleName().toString();

                Computed computed = executableElement.getAnnotation(Computed.class);
                Watch watch = executableElement.getAnnotation(Watch.class);
                PropValidator propValidator = executableElement.getAnnotation(PropValidator.class);

                if (computed != null)
                {
                    addComputed(executableElement, computed, constructorBuilder);
                }
                else if (watch != null)
                {
                    String jsName = watch.propertyName();
                    constructorBuilder.addStatement("this.addJavaWatch($S, $S, $L)",
                        javaName,
                        jsName,
                        watch.isDeep());
                }
                else if (propValidator != null)
                {
                    String propertyName = propValidator.propertyName();
                    constructorBuilder.addStatement("this.addJavaPropValidator($S, $S)",
                        javaName,
                        propertyName);
                }
                else if ("render".equals(javaName) && GenerationUtil.hasInterface(processingEnv,
                    componentTypeElement.asType(),
                    HasRender.class))
                {
                    addRenderFunction(optionsClassBuilder);
                }
                else if (isHookMethod(processingEnv, componentTypeElement, executableElement))
                {
                    constructorBuilder.addStatement("this.addJavaLifecycleHook($S)", javaName);
                }
            });
    }

    /**
     * Add a computed property to our Options.
     * @param method The Java method (either the getter or setter of our Computed property)
     * @param computed Computed annotation
     * @param constructorBuilder The builder for the Constructor of our Options class
     */
    private void addComputed(ExecutableElement method, Computed computed,
        MethodSpec.Builder constructorBuilder)
    {
        String javaMethodName = method.getSimpleName().toString();

        ComputedKind kind = ComputedKind.GETTER;
        if ("void".equals(method.getReturnType().toString()))
            kind = ComputedKind.SETTER;

        constructorBuilder.addStatement("this.addJavaComputed($S, $S, $T.$L)",
            javaMethodName,
            GenerationUtil.getComputedPropertyName(computed, method.getSimpleName().toString()),
            ComputedKind.class,
            kind);
    }

    /**
     * Add a render function to our Options.
     * @param componentClassBuilder The builder for our Options class
     */
    private void addRenderFunction(Builder componentClassBuilder)
    {
        MethodSpec.Builder renderFunctionBuilder = MethodSpec
            .methodBuilder("render")
            .returns(Object.class)
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(JsMethod.class)
            .addParameter(CreateElementFunction.class, "createElementFunction")
            .addStatement(
                "Object componentRenderMethod = $T.get($T.get($T.get($T.get(this, $S), $S), $S), $S)",
                JsTools.class,
                JsTools.class,
                JsTools.class,
                JsTools.class,
                "$options",
                "vuegwt$vueComponentJsTypeConstructor",
                "prototype",
                "render")
            .addStatement("return $T.call($L, this, new $T($L))",
                JsTools.class,
                "componentRenderMethod",
                VNodeBuilder.class,
                "createElementFunction");

        componentClassBuilder.addMethod(renderFunctionBuilder.build());
    }

    private boolean isHookMethod(ProcessingEnvironment processingEnv,
        TypeElement componentTypeElement, ExecutableElement executableElement)
    {
        String methodJavaName = executableElement.getSimpleName().toString();

        return HOOKS_MAP.containsKey(methodJavaName) && GenerationUtil.hasInterface(processingEnv,
            componentTypeElement.asType(),
            HOOKS_MAP.get(methodJavaName));
    }

    private boolean isVisibleInJS(ExecutableElement executableElement)
    {
        return (hasComponentJsTypeAnnotation && executableElement
            .getModifiers()
            .contains(Modifier.PUBLIC)) || executableElement.getAnnotation(JsMethod.class) != null;
    }

    private boolean isVisibleInJS(VariableElement variableElement)
    {
        return (hasComponentJsTypeAnnotation && variableElement
            .getModifiers()
            .contains(Modifier.PUBLIC)) || variableElement.getAnnotation(JsProperty.class) != null;
    }

    /**
     * Transform a Java type name into a JavaScript type name.
     * Takes care of primitive types.
     * @param typeMirror A type to convert
     * @return A String representing the JavaScript type name
     */
    private String getNativeNameForJavaType(TypeMirror typeMirror)
    {
        TypeName typeName = TypeName.get(typeMirror);

        if (typeName.equals(TypeName.INT)
            || typeName.equals(TypeName.BYTE)
            || typeName.equals(TypeName.SHORT)
            || typeName.equals(TypeName.LONG)
            || typeName.equals(TypeName.FLOAT)
            || typeName.equals(TypeName.DOUBLE))
        {
            return "Number";
        }
        else if (typeName.equals(TypeName.BOOLEAN))
        {
            return "Boolean";
        }
        else if (typeName.equals(TypeName.get(String.class)) || typeName.equals(TypeName.CHAR))
        {
            return "String";
        }
        else if (typeMirror.toString().startsWith(JsArray.class.getCanonicalName()))
        {
            return "Array";
        }
        else
        {
            return "Object";
        }
    }
}
