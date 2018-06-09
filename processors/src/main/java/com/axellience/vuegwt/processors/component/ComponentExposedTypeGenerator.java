package com.axellience.vuegwt.processors.component;

import static com.axellience.vuegwt.processors.utils.ComponentGeneratorsUtil.getSuperComponentCount;
import static com.axellience.vuegwt.processors.utils.ComponentGeneratorsUtil.getSuperComponentType;
import static com.axellience.vuegwt.processors.utils.ComponentGeneratorsUtil.hasTemplate;
import static com.axellience.vuegwt.processors.utils.ComponentGeneratorsUtil.isFieldVisibleInJS;
import static com.axellience.vuegwt.processors.utils.ComponentGeneratorsUtil.isMethodVisibleInJS;
import static com.axellience.vuegwt.processors.utils.GeneratorsNameUtil.componentExposedTypeName;
import static com.axellience.vuegwt.processors.utils.GeneratorsNameUtil.componentFactoryName;
import static com.axellience.vuegwt.processors.utils.GeneratorsNameUtil.componentInjectedDependenciesName;
import static com.axellience.vuegwt.processors.utils.GeneratorsNameUtil.methodToEventName;
import static com.axellience.vuegwt.processors.utils.GeneratorsUtil.hasAnnotation;
import static com.axellience.vuegwt.processors.utils.GeneratorsUtil.hasInterface;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwt.core.annotations.component.Emit;
import com.axellience.vuegwt.core.annotations.component.HookMethod;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.annotations.component.PropDefault;
import com.axellience.vuegwt.core.annotations.component.PropValidator;
import com.axellience.vuegwt.core.annotations.component.Watch;
import com.axellience.vuegwt.core.client.VueGWT;
import com.axellience.vuegwt.core.client.component.ComponentExposedTypeConstructorFn;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import com.axellience.vuegwt.core.client.component.hooks.HasRender;
import com.axellience.vuegwt.core.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.core.client.component.options.computed.ComputedKind;
import com.axellience.vuegwt.core.client.vnode.VNode;
import com.axellience.vuegwt.core.client.vnode.builder.CreateElementFunction;
import com.axellience.vuegwt.core.client.vnode.builder.VNodeBuilder;
import com.axellience.vuegwt.core.client.vue.VueJsConstructor;
import com.axellience.vuegwt.processors.component.template.ComponentTemplateProcessor;
import com.axellience.vuegwt.processors.component.validators.DataFieldsValidator;
import com.axellience.vuegwt.processors.utils.ComponentGeneratorsUtil;
import com.axellience.vuegwt.processors.utils.GeneratorsUtil;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import elemental2.core.JsArray;
import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsType;

/**
 * Generate a ExposedType wrapper for the user Java {@link IsVueComponent}.
 * It will wrap any non JsInterop methods from the original
 * component to make them visible to JS.
 * It also provides the {@link VueComponentOptions} that will be passed down to Vue.js
 * to initialize our {@link VueJsConstructor}.
 * @author Adrien Baron
 */
public class ComponentExposedTypeGenerator
{
    private final ProcessingEnvironment processingEnv;
    private final Filer filer;
    private final Messager messager;
    private final Elements elements;
    private final ComponentTemplateProcessor componentTemplateProcessor;
    private final DataFieldsValidator dataFieldsValidator;

    public ComponentExposedTypeGenerator(ProcessingEnvironment processingEnvironment)
    {
        processingEnv = processingEnvironment;
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        elements = processingEnvironment.getElementUtils();
        componentTemplateProcessor = new ComponentTemplateProcessor(processingEnvironment);
        dataFieldsValidator = new DataFieldsValidator(processingEnvironment.getTypeUtils(), elements, messager);
    }

    public void generate(TypeElement component,
        ComponentInjectedDependenciesBuilder dependenciesBuilder)
    {
        // Template resource abstract class
        ClassName componentWithSuffixClassName = componentExposedTypeName(component);

        Builder componentExposedTypeBuilder =
            getComponentExposedTypeBuilder(component, componentWithSuffixClassName);

        // Add the getter for the Factory
        addGetFactoryMethod(component, componentExposedTypeBuilder);

        // Initialize Options getter builder
        MethodSpec.Builder optionsBuilder = getOptionsMethodBuilder(component);

        Set<ExecutableElement> hookMethodsFromInterfaces = getHookMethodsFromInterfaces(component);
        processData(component, optionsBuilder);
        processProps(component, optionsBuilder);
        processComputed(component, optionsBuilder, componentExposedTypeBuilder);
        processWatchers(component, optionsBuilder, componentExposedTypeBuilder);
        processPropValidators(component, optionsBuilder, componentExposedTypeBuilder);
        processPropDefaultValues(component, optionsBuilder, componentExposedTypeBuilder);
        processHooks(component, optionsBuilder, hookMethodsFromInterfaces);
        processTemplateMethods(component,
            optionsBuilder,
            componentExposedTypeBuilder,
            hookMethodsFromInterfaces);
        processInvalidEmitMethods(component);
        processRenderFunction(component, optionsBuilder, componentExposedTypeBuilder);
        createCreatedHook(component,
            optionsBuilder,
            componentExposedTypeBuilder,
            dependenciesBuilder);

        // Process the HTML template if there is one
        if (hasTemplate(processingEnv, component))
        {
            componentTemplateProcessor.processComponentTemplate(component,
                componentExposedTypeBuilder);
            optionsBuilder.addStatement(
                "options.initRenderFunctions(getRenderFunction(), getStaticRenderFunctions())");
        }

        // Finish building Options getter
        optionsBuilder.addStatement("return options");
        componentExposedTypeBuilder.addMethod(optionsBuilder.build());

        // And generate our Java Class
        GeneratorsUtil.toJavaFile(filer,
            componentExposedTypeBuilder,
            componentWithSuffixClassName,
            component);
    }

    /**
     * Create and return the builder for the ExposedType of our {@link IsVueComponent}.
     * @param component The {@link IsVueComponent} we are generating for
     * @param exposedTypeClassName The name of the generated ExposedType class
     * @return A Builder to build the class
     */
    private Builder getComponentExposedTypeBuilder(TypeElement component, ClassName exposedTypeClassName)
    {
        Builder componentExposedTypeBuilder = TypeSpec
            .classBuilder(exposedTypeClassName)
            .addModifiers(Modifier.PUBLIC)
            .superclass(TypeName.get(component.asType()))
            .addAnnotation(AnnotationSpec
                .builder(Generated.class)
                .addMember("value", "$S", ComponentExposedTypeGenerator.class.getCanonicalName())
                .addMember("date", "$S", new Date().toString())
                .addMember("comments", "$S", "https://github.com/Axellience/vue-gwt")
                .build());

        // Add @JsType annotation. This ensure this class is included.
        // As we use a class reference to use our Components, this class would be removed by GWT
        // tree shaking.
        componentExposedTypeBuilder.addAnnotation(AnnotationSpec
            .builder(JsType.class)
            .addMember("namespace", "$S", "VueGWTExposedTypesRepository")
            .addMember("name", "$S", component.getQualifiedName().toString().replaceAll("\\.", "_"))
            .build());

        return componentExposedTypeBuilder;
    }

    /**
     * Add a method to retrieve the Factory from the ExportedType
     * @param component The {@link IsVueComponent} we are generating for
     * @param componentExposedTypeBuilder Builder for the ExposedType class
     */
    private void addGetFactoryMethod(TypeElement component, Builder componentExposedTypeBuilder)
    {
        componentExposedTypeBuilder.addMethod(MethodSpec
            .methodBuilder("getVueComponentFactory")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(componentFactoryName(component))
            .addAnnotation(GeneratorsUtil.getUnusableByJSAnnotation())
            .addStatement("return $T.get()", componentFactoryName(component))
            .build());
    }

    /**
     * Create and return the builder for the method that creating the {@link VueComponentOptions}
     * for this {@link IsVueComponent}.
     * @param component The {@link IsVueComponent} we are generating for
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
            "options.setComponentExportedTypePrototype($T.getComponentExposedTypeConstructorFn($T.class).getPrototype())",
            VueGWT.class,
            component);

        return optionsMethodBuilder;
    }

    /**
     * Process data fields from the {@link IsVueComponent} Class.
     * @param component {@link IsVueComponent} to process
     * @param optionsBuilder A {@link MethodSpec.Builder} for the method that creates the
     * {@link VueComponentOptions}
     */
    private void processData(TypeElement component, MethodSpec.Builder optionsBuilder)
    {
        Component annotation = component.getAnnotation(Component.class);

        List<VariableElement> dataFields = ElementFilter
            .fieldsIn(component.getEnclosedElements())
            .stream()
            .filter(ComponentGeneratorsUtil::isFieldVisibleInJS)
            .filter(field -> field.getAnnotation(Prop.class) == null)
            .collect(Collectors.toList());

        if (dataFields.isEmpty())
            return;

        dataFields.forEach(dataFieldsValidator::validateComponentDataField);

        // Declare data fields
        String fieldNamesParameters = dataFields
            .stream()
            .map(field -> "\"" + field.getSimpleName() + "\"")
            .collect(Collectors.joining(", "));

        optionsBuilder.addStatement("options.initData($L, $L)",
            annotation.useFactory(),
            fieldNamesParameters);
    }

    /**
     * Process Vue Props from the {@link IsVueComponent} Class.
     * @param component {@link IsVueComponent} to process
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
                    printError("The field \""
                            + fieldName
                            + "\" annotated with @Prop must also be annotated with @JsProperty.",
                        component);
                }

                optionsBuilder.addStatement("options.addJavaProp($S, $L, $S)",
                    fieldName,
                    prop.required(),
                    prop.checkType() ? getNativeNameForJavaType(field.asType()) : null);
            });
    }

    /**
     * Process computed properties from the Component Class.
     * @param component {@link IsVueComponent} to process
     * @param optionsBuilder A {@link MethodSpec.Builder} for the method that creates the
     * {@link VueComponentOptions}
     * @param componentExposedTypeBuilder Builder for the ExposedType class
     */
    private void processComputed(TypeElement component, MethodSpec.Builder optionsBuilder,
        Builder componentExposedTypeBuilder)
    {
        getMethodsWithAnnotation(component, Computed.class).forEach(method -> {
            String methodName = method.getSimpleName().toString();

            ComputedKind kind = ComputedKind.GETTER;
            if ("void".equals(method.getReturnType().toString()))
                kind = ComputedKind.SETTER;

            String propertyName = GeneratorsUtil.getComputedPropertyName(method);
            optionsBuilder.addStatement("options.addJavaComputed($S, $S, $T.$L)",
                methodName,
                propertyName,
                ComputedKind.class,
                kind);

            addProxyExposedTypeMethodIfNecessary(componentExposedTypeBuilder, method);
        });

        addFieldsForComputedMethod(component, componentExposedTypeBuilder, new HashSet<>());
    }

    /**
     * Process template methods for our {@link IsVueComponent} class.
     * @param component {@link IsVueComponent} to process
     * @param optionsBuilder A {@link MethodSpec.Builder} for the method that creates the
     * {@link VueComponentOptions}
     * @param componentExposedTypeBuilder Builder for the ExposedType class
     * @param hookMethodsFromInterfaces Hook methods from the interface the {@link IsVueComponent}
     * implements
     */
    private void processTemplateMethods(TypeElement component, MethodSpec.Builder optionsBuilder,
        Builder componentExposedTypeBuilder, Set<ExecutableElement> hookMethodsFromInterfaces)
    {
        List<ExecutableElement> templateMethods = ElementFilter
            .methodsIn(component.getEnclosedElements())
            .stream()
            .filter(ComponentGeneratorsUtil::isMethodVisibleInTemplate)
            .filter(method -> !isHookMethod(component, method, hookMethodsFromInterfaces))
            .collect(Collectors.toList());

        templateMethods.forEach(method -> addProxyExposedTypeMethodIfNecessary(
            componentExposedTypeBuilder,
            method));

        // Declare methods in the component
        String methodNamesParameters = templateMethods
            .stream()
            .map(method -> "\"" + method.getSimpleName() + "\"")
            .collect(Collectors.joining(", "));
        optionsBuilder.addStatement("options.addMethods($L)", methodNamesParameters);
    }

    /**
     * Add fields for computed methods so they are visible in the template
     * @param component {@link IsVueComponent} to process
     * @param componentExposedTypeBuilder Builder for the ExposedType class
     * @param alreadyDone Already processed computed properties (in case there is a getter and a
     * setter, avoid creating the field twice).
     */
    private void addFieldsForComputedMethod(TypeElement component,
        Builder componentExposedTypeBuilder, Set<String> alreadyDone)
    {
        getMethodsWithAnnotation(component, Computed.class).forEach(method -> {
            String propertyName = GeneratorsUtil.getComputedPropertyName(method);

            if (alreadyDone.contains(propertyName))
                return;

            TypeMirror propertyType;
            if ("void".equals(method.getReturnType().toString()))
                propertyType = method.getParameters().get(0).asType();
            else
                propertyType = method.getReturnType();

            componentExposedTypeBuilder.addField(TypeName.get(propertyType),
                propertyName,
                Modifier.PUBLIC);
            alreadyDone.add(propertyName);
        });

        getSuperComponentType(component).ifPresent(superComponent -> addFieldsForComputedMethod(
            superComponent,
            componentExposedTypeBuilder,
            alreadyDone));
    }

    /**
     * Process watchers from the Component Class.
     * @param component {@link IsVueComponent} to process
     * @param optionsBuilder A {@link MethodSpec.Builder} for the method that creates the
     * {@link VueComponentOptions}
     */
    private void processWatchers(TypeElement component, MethodSpec.Builder optionsBuilder,
        Builder componentExposedTypeBuilder)
    {
        getMethodsWithAnnotation(component, Watch.class).forEach(method -> {
            Watch watch = method.getAnnotation(Watch.class);

            optionsBuilder.addStatement("options.addJavaWatch($S, $S, $L)",
                method.getSimpleName().toString(),
                watch.value(),
                watch.isDeep());

            addProxyExposedTypeMethodIfNecessary(componentExposedTypeBuilder, method);
        });
    }

    /**
     * Process prop validators from the Component Class.
     * @param component {@link IsVueComponent} to process
     * @param optionsBuilder A {@link MethodSpec.Builder} for the method that creates the
     * {@link VueComponentOptions}
     */
    private void processPropValidators(TypeElement component, MethodSpec.Builder optionsBuilder,
        Builder componentExposedTypeBuilder)
    {
        getMethodsWithAnnotation(component, PropValidator.class).forEach(method -> {
            PropValidator propValidator = method.getAnnotation(PropValidator.class);

            if (!TypeName.get(method.getReturnType()).equals(TypeName.BOOLEAN))
            {
                printError("Method "
                    + method.getSimpleName()
                    + " annotated with PropValidator must return a boolean.", component);
            }

            String propertyName = propValidator.value();
            optionsBuilder.addStatement("options.addJavaPropValidator($S, $S)",
                method.getSimpleName().toString(),
                propertyName);

            addProxyExposedTypeMethodIfNecessary(componentExposedTypeBuilder, method);
        });
    }

    /**
     * Process prop default values from the Component Class.
     * @param component {@link IsVueComponent} to process
     * @param optionsBuilder A {@link MethodSpec.Builder} for the method that creates the
     * {@link VueComponentOptions}
     */
    private void processPropDefaultValues(TypeElement component, MethodSpec.Builder optionsBuilder,
        Builder componentExposedTypeBuilder)
    {
        getMethodsWithAnnotation(component, PropDefault.class).forEach(method -> {
            PropDefault propValidator = method.getAnnotation(PropDefault.class);

            String propertyName = propValidator.value();
            optionsBuilder.addStatement("options.addJavaPropDefaultValue($S, $S)",
                method.getSimpleName().toString(),
                propertyName);

            addProxyExposedTypeMethodIfNecessary(componentExposedTypeBuilder, method);
        });
    }

    /**
     * Process hook methods from the Component Class.
     * @param component {@link IsVueComponent} to process
     * @param optionsBuilder A {@link MethodSpec.Builder} for the method that creates the
     * {@link VueComponentOptions}
     * @param hookMethodsFromInterfaces Hook methods from the interface the {@link IsVueComponent}
     * implements
     */
    private void processHooks(TypeElement component, MethodSpec.Builder optionsBuilder,
        Set<ExecutableElement> hookMethodsFromInterfaces)
    {
        ElementFilter
            .methodsIn(component.getEnclosedElements())
            .stream()
            .filter(method -> isHookMethod(component, method, hookMethodsFromInterfaces))
            .forEach(method -> optionsBuilder.addStatement("options.addHookMethod($S)",
                method.getSimpleName().toString()));
    }

    /**
     * Return all hook methods from the implemented interfaces
     * @param component The Component
     * @return Hook methods that must be overridden in the Component
     */
    private Set<ExecutableElement> getHookMethodsFromInterfaces(TypeElement component)
    {
        return component
            .getInterfaces()
            .stream()
            .map(DeclaredType.class::cast)
            .map(DeclaredType::asElement)
            .map(TypeElement.class::cast)
            .flatMap(typeElement -> ElementFilter
                .methodsIn(typeElement.getEnclosedElements())
                .stream())
            .filter(method -> hasAnnotation(method, HookMethod.class))
            .peek(hookMethod -> validateHookMethod(hookMethod, component))
            .collect(Collectors.toSet());
    }

    private void validateHookMethod(ExecutableElement hookMethod, TypeElement component)
    {
        if (!isMethodVisibleInJS(hookMethod))
            printError("Method "
                + hookMethod.getSimpleName()
                + " annotated with HookMethod should also have @JsMethod property.", component);
    }

    /**
     * Process the render function from the Component Class if it has one.
     * @param component {@link IsVueComponent} to process
     * @param optionsBuilder A {@link MethodSpec.Builder} for the method that creates the
     * {@link VueComponentOptions}
     * @param componentExposedTypeBuilder Builder for the ExposedType class
     */
    private void processRenderFunction(TypeElement component, MethodSpec.Builder optionsBuilder,
        Builder componentExposedTypeBuilder)
    {
        if (!hasInterface(processingEnv, component.asType(), HasRender.class))
            return;

        componentExposedTypeBuilder.addMethod(MethodSpec
            .methodBuilder("vuegwt$render")
            .addModifiers(Modifier.PUBLIC)
            .returns(VNode.class)
            .addParameter(CreateElementFunction.class, "createElementFunction")
            .addStatement("return super.render(new $T(createElementFunction))", VNodeBuilder.class)
            .build());

        // Register the render method
        optionsBuilder.addStatement("options.addHookMethod($S, $S)", "render", "vuegwt$render");
    }

    /**
     * Create the "created" hook method. This method will be called on each Component when it's
     * created.
     * It will inject dependencies if any, and call the {@link ComponentExposedTypeConstructorFn} on the
     * newly created instance.
     * @param component {@link IsVueComponent} to process
     * @param optionsBuilder A {@link MethodSpec.Builder} for the method that creates the
     * {@link VueComponentOptions}
     * @param componentExposedTypeBuilder Builder for the ExposedType class
     * @param dependenciesBuilder Builder for our component dependencies, needed here to inject the
     * dependencies in the instance
     */
    private void createCreatedHook(TypeElement component, MethodSpec.Builder optionsBuilder,
        Builder componentExposedTypeBuilder,
        ComponentInjectedDependenciesBuilder dependenciesBuilder)
    {
        String hasRunCreatedFlagName = "vuegwt$hrc_" + getSuperComponentCount(component);
        componentExposedTypeBuilder.addField(boolean.class, hasRunCreatedFlagName, Modifier.PUBLIC);

        MethodSpec.Builder createdMethodBuilder =
            MethodSpec.methodBuilder("vuegwt$created").addModifiers(Modifier.PUBLIC);

        // Avoid infinite recursion in case calling the Java constructor calls Vue.js constructor
        // This can happen when extending an existing JS component
        createdMethodBuilder.addStatement("if ($L) return", hasRunCreatedFlagName);
        createdMethodBuilder.addStatement("$L = true", hasRunCreatedFlagName);

        injectDependencies(component, dependenciesBuilder, createdMethodBuilder);
        callConstructor(component, createdMethodBuilder);

        if (hasInterface(processingEnv, component.asType(), HasCreated.class))
        {
            createdMethodBuilder.addStatement("super.created()");
        }

        componentExposedTypeBuilder.addMethod(createdMethodBuilder.build());

        // Register the hook
        optionsBuilder.addStatement("options.addHookMethod($S, $S)", "created", "vuegwt$created");
    }

    /**
     * Inject the dependencies in the instance if needed. We do that by injecting an instance of an
     * object generated by {@link ComponentInjectedDependenciesBuilder}. We then copy the fields
     * of this object, and call methods that needs injection.
     * @param component {@link IsVueComponent} to process
     * @param dependenciesBuilder Builder for our component dependencies, needed here to inject the
     * dependencies in the instance
     * @param createdMethodBuilder Builder for our Create method
     */
    private void injectDependencies(TypeElement component,
        ComponentInjectedDependenciesBuilder dependenciesBuilder,
        MethodSpec.Builder createdMethodBuilder)
    {
        if (!dependenciesBuilder.hasInjectedDependencies())
            return;

        createDependenciesInstance(component, createdMethodBuilder);
        copyDependenciesFields(dependenciesBuilder, createdMethodBuilder);
        callMethodsWithDependencies(dependenciesBuilder, createdMethodBuilder);
    }

    private void createDependenciesInstance(TypeElement component,
        MethodSpec.Builder createdMethodBuilder)
    {
        ClassName dependenciesName = componentInjectedDependenciesName(component);
        createdMethodBuilder.addStatement(
            "$T dependencies = ($T) vue().$L.getProvider($T.class).get()",
            dependenciesName,
            dependenciesName,
            "$options()",
            component);
    }

    /**
     * Emit an error message for every method annotated with {@link Emit} that are not also
     * annotated with {@link JsMethod}.
     * @param component {@link IsVueComponent} to process
     */
    private void processInvalidEmitMethods(TypeElement component)
    {
        ElementFilter
            .methodsIn(component.getEnclosedElements())
            .stream()
            .filter(method -> hasAnnotation(method, Emit.class))
            .filter(method -> !hasAnnotation(method, JsMethod.class))
            .forEach(invalidEmitMethod -> printError("The method \"" + invalidEmitMethod
                    .getSimpleName()
                    .toString() + "\" annotated with @Emit must also be annotated with @JsMethod.",
                component));
    }

    private void copyDependenciesFields(ComponentInjectedDependenciesBuilder dependenciesBuilder,
        MethodSpec.Builder createdMethodBuilder)
    {
        dependenciesBuilder
            .getInjectedFieldsName()
            .forEach(fieldName -> createdMethodBuilder.addStatement("super.$L = dependencies.$L",
                fieldName,
                fieldName));
    }

    private void callMethodsWithDependencies(
        ComponentInjectedDependenciesBuilder dependenciesBuilder,
        MethodSpec.Builder createdMethodBuilder)
    {
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

    /**
     * Call our {@link IsVueComponent} constructor. Pass injected parameters if needed.
     * @param component {@link IsVueComponent} to process
     * @param createdMethodBuilder Builder for our Create method
     */
    private void callConstructor(TypeElement component, MethodSpec.Builder createdMethodBuilder)
    {
        createdMethodBuilder.addStatement(
            "$T.getComponentExposedTypeConstructorFn($T.class).initComponentInstanceProperties(this)",
            VueGWT.class,
            component);
    }

    /**
     * Generate a JsInterop proxy method for a {@link IsVueComponent} method.
     * This proxy will keep the same name in JS and can be therefore passed to Vue to
     * configure our {@link IsVueComponent}.
     * @param componentExposedTypeBuilder Builder for the ExposedType class
     * @param originalMethod Method to proxify
     */
    private void addProxyExposedTypeMethodIfNecessary(Builder componentExposedTypeBuilder,
        ExecutableElement originalMethod)
    {
        Emit emitAnnotation = originalMethod.getAnnotation(Emit.class);
        if (isMethodVisibleInJS(originalMethod) && emitAnnotation == null)
            return;

        MethodSpec.Builder proxyMethodBuilder = MethodSpec
            .methodBuilder(originalMethod.getSimpleName().toString())
            .addModifiers(Modifier.PUBLIC)
            .returns(ClassName.get(originalMethod.getReturnType()));

        originalMethod
            .getParameters()
            .forEach(parameter -> proxyMethodBuilder.addParameter(TypeName.get(parameter.asType()),
                parameter.getSimpleName().toString()));

        String methodCallParameters = getSuperMethodCallParameters(originalMethod);
        boolean hasReturnValue = !"void".equals(originalMethod.getReturnType().toString());
        if (hasReturnValue)
        {
            proxyMethodBuilder.addStatement("$T result = super.$L($L)",
                originalMethod.getReturnType(),
                originalMethod.getSimpleName().toString(),
                methodCallParameters);
        }
        else
        {
            proxyMethodBuilder.addStatement("super.$L($L)",
                originalMethod.getSimpleName().toString(),
                methodCallParameters);
        }

        if (emitAnnotation != null)
            addEmitEventCall(originalMethod, proxyMethodBuilder, methodCallParameters);

        if (hasReturnValue)
            proxyMethodBuilder.addStatement("return result");

        componentExposedTypeBuilder.addMethod(proxyMethodBuilder.build());
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
     * Add a call to emit an event at the end of the function
     * @param originalMethod Method we are emitting an event for
     * @param proxyMethodBuilder Method we are building
     * @param methodCallParameters Chained parameters name of the method
     */
    private void addEmitEventCall(ExecutableElement originalMethod,
        MethodSpec.Builder proxyMethodBuilder, String methodCallParameters)
    {
        String methodName = "$emit";
        if (methodCallParameters != null && !"".equals(methodCallParameters))
        {
            proxyMethodBuilder.addStatement("vue().$L($S, $L)",
                methodName,
                methodToEventName(originalMethod),
                methodCallParameters);
        }
        else
        {
            proxyMethodBuilder.addStatement("vue().$L($S)",
                methodName,
                methodToEventName(originalMethod));
        }
    }

    /**
     * Return true of the given method is a proxy method
     * @param component {@link IsVueComponent} this method belongs to
     * @param method The java method to check
     * @param hookMethodsFromInterfaces All the hook methods in the implement interfaces
     * @return True if this method is a hook method, false otherwise
     */
    private boolean isHookMethod(TypeElement component, ExecutableElement method,
        Set<ExecutableElement> hookMethodsFromInterfaces)
    {
        if (hasAnnotation(method, HookMethod.class))
        {
            validateHookMethod(method, component);
            return true;
        }

        for (ExecutableElement hookMethodsFromInterface : hookMethodsFromInterfaces)
        {
            if (elements.overrides(method, hookMethodsFromInterface, component))
                return true;
        }

        return false;
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

    private void printError(String message, TypeElement component)
    {
        messager.printMessage(Kind.ERROR,
            message + " In VueComponent: " + component.getQualifiedName());
    }
}
