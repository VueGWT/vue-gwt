package com.axellience.vuegwt.jsr69.component;

import com.axellience.vuegwt.client.VueGWT;
import com.axellience.vuegwt.client.component.ComponentJavaConstructor;
import com.axellience.vuegwt.client.component.HasRender;
import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.hooks.HasActivated;
import com.axellience.vuegwt.client.component.hooks.HasBeforeCreate;
import com.axellience.vuegwt.client.component.hooks.HasBeforeDestroy;
import com.axellience.vuegwt.client.component.hooks.HasBeforeMount;
import com.axellience.vuegwt.client.component.hooks.HasBeforeUpdate;
import com.axellience.vuegwt.client.component.hooks.HasDeactivated;
import com.axellience.vuegwt.client.component.hooks.HasDestroyed;
import com.axellience.vuegwt.client.component.hooks.HasMounted;
import com.axellience.vuegwt.client.component.hooks.HasUpdated;
import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.client.component.options.computed.ComputedKind;
import com.axellience.vuegwt.client.component.template.TemplateResource;
import com.axellience.vuegwt.client.jsnative.jstypes.JsArray;
import com.axellience.vuegwt.client.tools.JsTools;
import com.axellience.vuegwt.client.vnode.VNode;
import com.axellience.vuegwt.client.vnode.builder.CreateElementFunction;
import com.axellience.vuegwt.client.vnode.builder.VNodeBuilder;
import com.axellience.vuegwt.client.vue.VueJsConstructor;
import com.axellience.vuegwt.jsr69.GenerationUtil;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.Computed;
import com.axellience.vuegwt.jsr69.component.annotations.Prop;
import com.axellience.vuegwt.jsr69.component.annotations.PropValidator;
import com.axellience.vuegwt.jsr69.component.annotations.Watch;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsType;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.axellience.vuegwt.jsr69.GenerationNameUtil.*;
import static com.axellience.vuegwt.jsr69.GenerationUtil.hasAnnotation;
import static com.axellience.vuegwt.jsr69.GenerationUtil.hasInterface;
import static com.axellience.vuegwt.jsr69.component.ComponentGenerationUtil.getTemplateVisibleMethods;
import static com.axellience.vuegwt.jsr69.component.ComponentGenerationUtil.hasTemplate;
import static com.axellience.vuegwt.jsr69.component.ComponentGenerationUtil.isFieldVisibleInJS;

/**
 * Generate the {@link TemplateResource} for the user Java {@link VueComponent}.
 * This {@link TemplateResource} will wrap any non JsInterop methods from the original
 * component to make them visible to JS.
 * It also provides the {@link VueComponentOptions} that will be passed down to Vue.js
 * to initialize our {@link VueJsConstructor}.
 * @author Adrien Baron
 */
public class ComponentJsTypeGenerator
{
    private final ProcessingEnvironment processingEnv;
    private final Filer filer;
    private final Messager messager;

    private static Map<String, Class> HOOKS_MAP = new HashMap<>();

    static
    {
        // Init the map of lifecycle hooks for fast type type check
        // The "created" hook is not here because it is always generated
        HOOKS_MAP.put("beforeCreate", HasBeforeCreate.class);
        HOOKS_MAP.put("beforeMount", HasBeforeMount.class);
        HOOKS_MAP.put("mounted", HasMounted.class);
        HOOKS_MAP.put("beforeUpdate", HasBeforeUpdate.class);
        HOOKS_MAP.put("updated", HasUpdated.class);
        HOOKS_MAP.put("activated", HasActivated.class);
        HOOKS_MAP.put("deactivated", HasDeactivated.class);
        HOOKS_MAP.put("beforeDestroy", HasBeforeDestroy.class);
        HOOKS_MAP.put("destroyed", HasDestroyed.class);
    }

    public ComponentJsTypeGenerator(ProcessingEnvironment processingEnvironment)
    {
        processingEnv = processingEnvironment;
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
    }

    public void generate(TypeElement component)
    {
        // Template resource abstract class
        ClassName componentWithSuffixClassName = componentJsTypeName(component);

        Builder componentJsTypeBuilder =
            getComponentJsTypeBuilder(component, componentWithSuffixClassName);

        // Initialize Options getter builder
        MethodSpec.Builder optionsBuilder = getOptionsMethodBuilder(component);

        ComponentInjectedDependenciesBuilder dependenciesBuilder =
            new ComponentInjectedDependenciesBuilder(processingEnv, component);

        processConstructor(component, componentJsTypeBuilder);
        processData(component, optionsBuilder);
        processProps(component, optionsBuilder);
        processComputed(component, optionsBuilder, componentJsTypeBuilder);
        processWatchers(component, optionsBuilder, componentJsTypeBuilder);
        processPropValidators(component, optionsBuilder, componentJsTypeBuilder);
        processTemplateVisibleMethods(component, componentJsTypeBuilder);
        processHooks(component, optionsBuilder);
        processRenderFunction(component, optionsBuilder, componentJsTypeBuilder);
        createCreatedHook(component, optionsBuilder, componentJsTypeBuilder, dependenciesBuilder);

        // Finish building Options getter
        optionsBuilder.addStatement("return options");
        componentJsTypeBuilder.addMethod(optionsBuilder.build());

        // And generate our Java Class
        GenerationUtil.toJavaFile(filer, componentJsTypeBuilder, componentWithSuffixClassName);
    }

    /**
     * Generate a {@link JsMethod} proxy for the {@link VueComponent} constructor.
     * This proxy will be exposed to JS and can be therefore used to initialize our
     * {@link VueComponent} instances.
     * @param component The {@link VueComponent} we are generating for
     * @param componentJsTypeBuilder Builder for the TemplateResource class
     */
    private void processConstructor(TypeElement component, Builder componentJsTypeBuilder)
    {
        List<ExecutableElement> constructors = ElementFilter
            .constructorsIn(component.getEnclosedElements())
            .stream()
            .filter(method -> !method.getModifiers().contains(Modifier.PRIVATE))
            .collect(Collectors.toList());

        if (constructors.size() > 1)
        {
            messager.printMessage(Kind.ERROR,
                component.getSimpleName().toString()
                    + " has "
                    + constructors.size()
                    + " non private constructors. VueComponents can have 0 or 1 non private constructors");
            return;
        }

        ExecutableElement originalConstructor = constructors.isEmpty() ? null : constructors.get(0);

        MethodSpec.Builder proxyMethodBuilder =
            MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);

        if (originalConstructor != null)
        {
            originalConstructor
                .getParameters()
                .forEach(parameter -> proxyMethodBuilder.addParameter(TypeName.get(parameter.asType()),
                    parameter.getSimpleName().toString()));
            String methodCallParameters = getSuperMethodCallParameters(originalConstructor);
            proxyMethodBuilder.addStatement("super($L)", methodCallParameters);
        }

        componentJsTypeBuilder.addMethod(proxyMethodBuilder.build());
    }

    /**
     * Create and return the builder for the {@link TemplateResource} of our {@link
     * VueComponent}.
     * @param component The {@link VueComponent} we are generating for
     * @param jsTypeClassName The name of the generated {@link TemplateResource} class
     * @return A Builder to build the class
     */
    private Builder getComponentJsTypeBuilder(TypeElement component, ClassName jsTypeClassName)
    {
        Builder componentJsTypeBuilder = TypeSpec
            .classBuilder(jsTypeClassName)
            .addModifiers(Modifier.PUBLIC)
            .superclass(TypeName.get(component.asType()));

        // Add @JsType annotation. This ensure this class is included.
        // As we use a class reference to use our Components, this class would be removed by GWT
        // tree shaking.
        componentJsTypeBuilder.addAnnotation(AnnotationSpec
            .builder(JsType.class)
            .addMember("namespace", "\"VueGWT.javaComponentConstructors\"")
            .addMember("name", "$S", component.getQualifiedName().toString().replaceAll("\\.", "_"))
            .build());

        // Add a block that registers the VueFactory for the VueComponent
        componentJsTypeBuilder.addStaticBlock(CodeBlock
            .builder()
            .addStatement("$T.onReady(() -> $T.register($S, () -> $T.get()))",
                VueGWT.class,
                VueGWT.class,
                component.getQualifiedName(),
                componentFactoryName(component))
            .build());

        return componentJsTypeBuilder;
    }

    /**
     * Create and return the builder for the method that creating the {@link VueComponentOptions}
     * for this {@link VueComponent}.
     * @param component The {@link VueComponent} we are generating for
     * @return A {@link MethodSpec.Builder} for the method that creates the {@link VueComponentOptions}
     */
    private MethodSpec.Builder getOptionsMethodBuilder(TypeElement component)
    {
        TypeName optionsTypeName =
            ParameterizedTypeName.get(ClassName.get(VueComponentOptions.class),
                ClassName.get(component));

        MethodSpec.Builder optionsMethodBuilder = MethodSpec
            .methodBuilder("getOptions")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(optionsTypeName)
            .addStatement("$T options = new $T()", optionsTypeName, optionsTypeName);

        Component annotation = component.getAnnotation(Component.class);

        if (!"".equals(annotation.name()))
            optionsMethodBuilder.addStatement("options.setName($S)", annotation.name());

        optionsMethodBuilder.addStatement(
            "options.setComponentJavaPrototype($T.getJavaConstructor($T.class).getPrototype())",
            VueGWT.class,
            component);

        if (hasTemplate(processingEnv, component))
        {
            optionsMethodBuilder.addStatement("options.setTemplateResource($T.INSTANCE.$L())",
                componentTemplateBundleName(component),
                COMPONENT_TEMPLATE_BUNDLE_METHOD_NAME);
        }

        return optionsMethodBuilder;
    }

    /**
     * Process data fields from the {@link VueComponent} Class.
     * @param component {@link VueComponent} to process
     * @param optionsBuilder A {@link MethodSpec.Builder} for the method that creates the
     * {@link VueComponentOptions}
     */
    private void processData(TypeElement component, MethodSpec.Builder optionsBuilder)
    {
        Component annotation = component.getAnnotation(Component.class);

        List<String> fieldsName = ElementFilter
            .fieldsIn(component.getEnclosedElements())
            .stream()
            .filter(ComponentGenerationUtil::isFieldVisibleInJS)
            .filter(field -> field.getAnnotation(Prop.class) == null)
            .map(field -> field.getSimpleName().toString())
            .collect(Collectors.toList());

        if (fieldsName.isEmpty())
            return;

        fieldsName.forEach(fieldName -> optionsBuilder.addStatement("options.addDataField($S)",
            fieldName));
        optionsBuilder.addStatement("options.initData($L)", annotation.useFactory());
    }

    /**
     * Process Vue Props from the {@link VueComponent} Class.
     * @param component {@link VueComponent} to process
     * @param optionsBuilder A {@link MethodSpec.Builder} for the method that creates the
     * {@link VueComponentOptions}
     */
    private void processProps(TypeElement component, MethodSpec.Builder optionsBuilder)
    {
        ElementFilter
            .fieldsIn(component.getEnclosedElements())
            .stream()
            .filter(field -> hasAnnotation(field, Prop.class))
            .forEach(field -> {
                String fieldName = field.getSimpleName().toString();
                Prop prop = field.getAnnotation(Prop.class);

                if (!isFieldVisibleInJS(field))
                {
                    messager.printMessage(Kind.ERROR,
                        "@Prop "
                            + fieldName
                            + " must also have @JsProperty annotation in VueComponent "
                            + component.getQualifiedName().toString()
                            + ".");
                }

                optionsBuilder.addStatement("options.addJavaProp($S, $L, $S)",
                    fieldName,
                    prop.required(),
                    prop.checkType() ? getNativeNameForJavaType(field.asType()) : null);
            });
    }

    /**
     * Process computed properties from the Component Class.
     * @param component {@link VueComponent} to process
     * @param optionsBuilder A {@link MethodSpec.Builder} for the method that creates the
     * {@link VueComponentOptions}
     */
    private void processComputed(TypeElement component, MethodSpec.Builder optionsBuilder,
        Builder componentJsTypeBuilder)
    {
        getMethodsWithAnnotation(component, Computed.class).forEach(method -> {
            String methodName = method.getSimpleName().toString();

            ComputedKind kind = ComputedKind.GETTER;
            if ("void".equals(method.getReturnType().toString()))
                kind = ComputedKind.SETTER;

            String propertyName = GenerationUtil.getComputedPropertyName(method);
            optionsBuilder.addStatement("options.addJavaComputed($S, $S, $T.$L)",
                methodName,
                propertyName,
                ComputedKind.class,
                kind);

            componentJsTypeBuilder.addMethod(createProxyJsTypeMethod(method));
        });
    }

    /**
     * Process watchers from the Component Class.
     * @param component {@link VueComponent} to process
     * @param optionsBuilder A {@link MethodSpec.Builder} for the method that creates the
     * {@link VueComponentOptions}
     */
    private void processWatchers(TypeElement component, MethodSpec.Builder optionsBuilder,
        Builder componentJsTypeBuilder)
    {
        getMethodsWithAnnotation(component, Watch.class).forEach(method -> {
            Watch watch = method.getAnnotation(Watch.class);

            optionsBuilder.addStatement("options.addJavaWatch($S, $S, $L)",
                method.getSimpleName().toString(),
                watch.propertyName(),
                watch.isDeep());

            componentJsTypeBuilder.addMethod(createProxyJsTypeMethod(method));
        });
    }

    /**
     * Process prop validators from the Component Class.
     * @param component {@link VueComponent} to process
     * @param optionsBuilder A {@link MethodSpec.Builder} for the method that creates the
     * {@link VueComponentOptions}
     */
    private void processPropValidators(TypeElement component, MethodSpec.Builder optionsBuilder,
        Builder componentJsTypeBuilder)
    {
        getMethodsWithAnnotation(component, PropValidator.class).forEach(method -> {
            PropValidator propValidator = method.getAnnotation(PropValidator.class);

            String propertyName = propValidator.propertyName();
            optionsBuilder.addStatement("options.addJavaPropValidator($S, $S)",
                method.getSimpleName().toString(),
                propertyName);

            componentJsTypeBuilder.addMethod(createProxyJsTypeMethod(method));
        });
    }

    /**
     * Process hook methods from the Component Class.
     * @param component {@link VueComponent} to process
     * @param optionsBuilder A {@link MethodSpec.Builder} for the method that creates the
     * {@link VueComponentOptions}
     */
    private void processHooks(TypeElement component, MethodSpec.Builder optionsBuilder)
    {
        ElementFilter
            .methodsIn(component.getEnclosedElements())
            .stream()
            .filter(method -> isHookMethod(component, method))
            .forEach(method -> optionsBuilder.addStatement("options.addRootJavaMethod($S)",
                method.getSimpleName().toString()));
    }

    /**
     * Process the render function from the Component Class if it has one.
     * @param component {@link VueComponent} to process
     * @param optionsBuilder A {@link MethodSpec.Builder} for the method that creates the
     * {@link VueComponentOptions}
     * @param componentJsTypeBuilder Builder for the TemplateResource class
     */
    private void processRenderFunction(TypeElement component, MethodSpec.Builder optionsBuilder,
        Builder componentJsTypeBuilder)
    {
        if (!hasInterface(processingEnv, component.asType(), HasRender.class))
            return;

        componentJsTypeBuilder.addMethod(MethodSpec
            .methodBuilder("vuegwt$render")
            .addModifiers(Modifier.PUBLIC)
            .returns(VNode.class)
            .addParameter(CreateElementFunction.class, "createElementFunction")
            .addStatement("return super.render(new $T(createElementFunction))", VNodeBuilder.class)
            .build());

        // Register the render method
        optionsBuilder.addStatement("options.addRootJavaMethod($S, $S)", "render", "vuegwt$render");
    }

    /**
     * Create the "created" hook method. This method will be called on each Component when it's
     * created.
     * It will inject dependencies if any, and call the {@link ComponentJavaConstructor} on the
     * newly created instance.
     * @param component {@link VueComponent} to process
     * @param optionsBuilder A {@link MethodSpec.Builder} for the method that creates the
     * {@link VueComponentOptions}
     * @param componentJsTypeBuilder Builder for the TemplateResource class
     * @param dependenciesBuilder Builder for our component dependencies, needed here to inject the
     * dependencies in the instance
     */
    private void createCreatedHook(TypeElement component, MethodSpec.Builder optionsBuilder,
        Builder componentJsTypeBuilder, ComponentInjectedDependenciesBuilder dependenciesBuilder)
    {
        MethodSpec.Builder createdMethodBuilder =
            MethodSpec.methodBuilder("vuegwt$created").addModifiers(Modifier.PUBLIC);

        injectDependencies(component, dependenciesBuilder, createdMethodBuilder);
        callConstructor(component, dependenciesBuilder, createdMethodBuilder);

        componentJsTypeBuilder.addMethod(createdMethodBuilder.build());

        // Register the hook
        optionsBuilder.addStatement("options.addRootJavaMethod($S, $S)",
            "created",
            "vuegwt$created");
    }

    private void injectDependencies(TypeElement component,
        ComponentInjectedDependenciesBuilder dependenciesBuilder,
        MethodSpec.Builder createdMethodBuilder)
    {
        if (!dependenciesBuilder.hasDependencies())
            return;

        ClassName dependenciesName = componentInjectedDependenciesName(component);
        createdMethodBuilder.addStatement(
            "$T dependencies = ($T) this.$L.getProvider($T.class).get()",
            dependenciesName,
            dependenciesName,
            "$options()",
            component);

        // Copy fields
        dependenciesBuilder
            .getInjectedFieldsName()
            .forEach(fieldName -> createdMethodBuilder.addStatement("$L = dependencies.$L",
                fieldName,
                fieldName));

        // Call methods
        for (Entry<String, List<String>> methodNameParametersEntry : dependenciesBuilder
            .getInjectedParametersByMethod()
            .entrySet())
        {
            String methodName = methodNameParametersEntry.getKey();
            List<String> callParameters = methodNameParametersEntry
                .getValue()
                .stream()
                .map(parameterName -> "dependencies." + parameterName)
                .collect(Collectors.toList());

            createdMethodBuilder.addStatement("$L($L)",
                methodName,
                String.join(", ", callParameters));
        }
    }

    private void callConstructor(TypeElement component,
        ComponentInjectedDependenciesBuilder dependenciesBuilder,
        MethodSpec.Builder createdMethodBuilder)
    {
        createdMethodBuilder.addStatement("Object javaConstructor = $T.getJavaConstructor($T.class)",
            VueGWT.class,
            component);

        List<String> injectedParameters = dependenciesBuilder.getInjectedConstructorParameters();
        String callParameters = "";
        if (!injectedParameters.isEmpty())
        {
            callParameters = ", " + injectedParameters
                .stream()
                .map(param -> "dependencies." + param)
                .collect(Collectors.joining(", "));
        }

        createdMethodBuilder.addStatement("$T.call(javaConstructor, this$L)",
            JsTools.class,
            callParameters);
    }

    private void processTemplateVisibleMethods(TypeElement component,
        Builder componentJsTypeBuilder)
    {
        getTemplateVisibleMethods(component)
            .stream()
            .map(this::createProxyJsTypeMethod)
            .forEach(componentJsTypeBuilder::addMethod);
    }

    /**
     * Generate a JsInterop proxy method for a {@link VueComponent} method.
     * This proxy will keep the same name in JS and can be therefore passed to Vue to
     * configure our {@link VueComponent}.
     * @param originalMethod Method to proxify
     */
    private MethodSpec createProxyJsTypeMethod(ExecutableElement originalMethod)
    {
        MethodSpec.Builder proxyMethodBuilder = MethodSpec
            .methodBuilder(originalMethod.getSimpleName().toString())
            .addModifiers(Modifier.PUBLIC)
            .returns(ClassName.get(originalMethod.getReturnType()));

        originalMethod
            .getParameters()
            .forEach(parameter -> proxyMethodBuilder.addParameter(TypeName.get(parameter.asType()),
                parameter.getSimpleName().toString()));

        String methodCallParameters = getSuperMethodCallParameters(originalMethod);

        String returnStatement = "";
        if (!"void".equals(originalMethod.getReturnType().toString()))
            returnStatement = "return ";

        proxyMethodBuilder.addStatement(returnStatement + "super.$L($L)",
            originalMethod.getSimpleName().toString(),
            methodCallParameters);

        return proxyMethodBuilder.build();
    }

    /**
     * Return the list of parameters name to pass to the super call on proxy methods.
     * @param sourceMethod The source method
     * @return A string which is the list of parameters name joined by ", "
     */
    private String getSuperMethodCallParameters(ExecutableElement sourceMethod)
    {
        return sourceMethod
            .getParameters()
            .stream()
            .map(parameter -> parameter.getSimpleName().toString())
            .collect(Collectors.joining(", "));
    }

    /**
     * Return true of the given method is a proxy method
     * @param component {@link VueComponent} this method belongs to
     * @param method The java method to check
     * @return True if this method is a hook method, false otherwise
     */
    private boolean isHookMethod(TypeElement component, ExecutableElement method)
    {
        String methodJavaName = method.getSimpleName().toString();

        return HOOKS_MAP.containsKey(methodJavaName) && hasInterface(processingEnv,
            component.asType(),
            HOOKS_MAP.get(methodJavaName));
    }

    private Stream<ExecutableElement> getMethodsWithAnnotation(TypeElement component,
        Class<? extends Annotation> annotation)
    {
        return ElementFilter
            .methodsIn(component.getEnclosedElements())
            .stream()
            .filter(method -> hasAnnotation(method, annotation));
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
