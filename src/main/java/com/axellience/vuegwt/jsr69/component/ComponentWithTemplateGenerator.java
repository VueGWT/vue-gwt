package com.axellience.vuegwt.jsr69.component;

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
import com.axellience.vuegwt.client.component.template.ComponentWithTemplate;
import com.axellience.vuegwt.client.jsnative.jstypes.JsArray;
import com.axellience.vuegwt.client.vnode.VNode;
import com.axellience.vuegwt.client.vnode.builder.CreateElementFunction;
import com.axellience.vuegwt.client.vnode.builder.VNodeBuilder;
import com.axellience.vuegwt.jsr69.GenerationUtil;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.Computed;
import com.axellience.vuegwt.jsr69.component.annotations.Prop;
import com.axellience.vuegwt.jsr69.component.annotations.PropValidator;
import com.axellience.vuegwt.jsr69.component.annotations.Watch;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.axellience.vuegwt.jsr69.component.constructor.AbstractVueComponentConstructorGenerator.CONSTRUCTOR_SUFFIX;

/**
 * Generate the TemplateProvider and {@link ComponentWithTemplate} for the user Java Components.
 * @author Adrien Baron
 */
public class ComponentWithTemplateGenerator
{
    public static String TEMPLATE_BUNDLE_SUFFIX = "TemplateBundle";
    public static String TEMPLATE_BUNDLE_METHOD_NAME = "template";
    public static String WITH_TEMPLATE_SUFFIX = "WithTemplate";

    private final ProcessingEnvironment processingEnv;
    private final Filer filer;
    private final Elements elementsUtils;

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

    private boolean hasComponentJsTypeAnnotation;

    public ComponentWithTemplateGenerator(ProcessingEnvironment processingEnvironment)
    {
        processingEnv = processingEnvironment;
        filer = processingEnvironment.getFiler();
        elementsUtils = processingEnvironment.getElementUtils();
    }

    public void generate(TypeElement component)
    {
        // Template resource abstract class
        String packageName = elementsUtils.getPackageOf(component).getQualifiedName().toString();
        String generatedClassName = component.getSimpleName().toString() + WITH_TEMPLATE_SUFFIX;

        Builder componentWithTemplateBuilder =
            getComponentWithTemplateBuilder(component, generatedClassName);

        hasComponentJsTypeAnnotation = component.getAnnotation(JsType.class) != null;

        // Initialize Options getter builder
        MethodSpec.Builder optionsBuilder = getOptionsMethodBuilder(component);

        processData(component, optionsBuilder);
        processProps(component, optionsBuilder);
        processComputed(component, optionsBuilder, componentWithTemplateBuilder);
        processWatchers(component, optionsBuilder, componentWithTemplateBuilder);
        processPropValidators(component, optionsBuilder, componentWithTemplateBuilder);
        processHooks(component, optionsBuilder);
        processRenderFunction(component, optionsBuilder, componentWithTemplateBuilder);

        // Finish building Options getter
        optionsBuilder.addStatement("return options");
        componentWithTemplateBuilder.addMethod(optionsBuilder.build());

        // And generate our Java Class
        GenerationUtil.toJavaFile(filer,
            componentWithTemplateBuilder,
            packageName,
            generatedClassName,
            component);
    }

    /**
     * Create and return the builder for the {@link ComponentWithTemplate} of our {@link
     * VueComponent}.
     * @param component The {@link VueComponent} we are generating for
     * @param generatedClassName The name of the generated {@link ComponentWithTemplate} class
     * @return A Builder to build the class
     */
    private Builder getComponentWithTemplateBuilder(TypeElement component,
        String generatedClassName)
    {
        Builder componentWithTemplateBuilder = TypeSpec
            .classBuilder(generatedClassName)
            .addModifiers(Modifier.PUBLIC)
            .addModifiers(Modifier.ABSTRACT)
            .superclass(TypeName.get(component.asType()))
            .addSuperinterface(ParameterizedTypeName.get(ClassName.get(ComponentWithTemplate.class),
                ClassName.get(component)));

        // Add @JsType annotation. This ensure this class is included.
        // As we use a class reference to use our Components, this class would be removed by GWT
        // tree shaking.
        componentWithTemplateBuilder.addAnnotation(AnnotationSpec
            .builder(JsType.class)
            .addMember("namespace", "\"VueGWT.componentWithTemplateConstructors\"")
            .addMember("name", "$S", component.getQualifiedName().toString().replaceAll("\\.", "_"))
            .build());

        // Add a block that registers the VueConstructor for the VueComponent
        componentWithTemplateBuilder.addStaticBlock(CodeBlock
            .builder()
            .addStatement("$T.onReady(() -> $T.register($S, $T.getSupplier()))",
                VueGWT.class,
                VueGWT.class,
                component.getQualifiedName(),
                ClassName.bestGuess(component.getQualifiedName() + CONSTRUCTOR_SUFFIX))
            .build());
        return componentWithTemplateBuilder;
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

        String packageName = elementsUtils.getPackageOf(component).getQualifiedName().toString();
        String className = component.getSimpleName().toString();

        optionsMethodBuilder.addStatement("options.setComponentWithTemplate($T.INSTANCE.$L())",
            ClassName.get(packageName, className + TEMPLATE_BUNDLE_SUFFIX),
            TEMPLATE_BUNDLE_METHOD_NAME);

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
            .filter(this::isVisibleInJS)
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
            .filter(field -> field.getAnnotation(Prop.class) != null)
            .forEach(field -> {
                String fieldName = field.getSimpleName().toString();
                Prop prop = field.getAnnotation(Prop.class);

                if (!isVisibleInJS(field))
                    throw new RuntimeException("@Prop "
                        + fieldName
                        + " must also have @JsProperty annotation in VueComponent "
                        + component.getQualifiedName().toString()
                        + ".");

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
     * @param componentWithTemplateBuilder Builder for the ComponentWithTemplate class
     */
    private void processComputed(TypeElement component, MethodSpec.Builder optionsBuilder,
        Builder componentWithTemplateBuilder)
    {
        getMethodsWithAnnotation(component, Computed.class).forEach(method -> {
            Computed computed = method.getAnnotation(Computed.class);
            String methodName = method.getSimpleName().toString();

            ComputedKind kind = ComputedKind.GETTER;
            if ("void".equals(method.getReturnType().toString()))
                kind = ComputedKind.SETTER;

            String propertyName = GenerationUtil.getComputedPropertyName(computed, methodName);
            optionsBuilder.addStatement("options.addJavaComputed($S, $S, $T.$L)",
                methodName,
                propertyName,
                ComputedKind.class,
                kind);

            generateProxyMethod(componentWithTemplateBuilder, method);
        });

        addComputedAttributes(component, componentWithTemplateBuilder, new HashSet<>());
    }

    /**
     * Add attributes for the computed properties.
     * This is needed for the Template expressions to compile in Java.
     * @param component The component we are currently processing
     * @param componentWithTemplateBuilder Builder for the ComponentWithTemplate class
     * @param alreadyDone Name of the computed properties already done
     */
    private void addComputedAttributes(TypeElement component, Builder componentWithTemplateBuilder,
        Set<String> alreadyDone)
    {
        getMethodsWithAnnotation(component, Computed.class).forEach(method -> {
            Computed computed = method.getAnnotation(Computed.class);
            String methodName = method.getSimpleName().toString();

            String propertyName = GenerationUtil.getComputedPropertyName(computed, methodName);
            if (alreadyDone.contains(propertyName))
                return;

            TypeName propertyType;
            if ("void".equals(method.getReturnType().toString()))
                propertyType = TypeName.get(method.getParameters().get(0).asType());
            else
                propertyType = TypeName.get(method.getReturnType());

            componentWithTemplateBuilder.addField(FieldSpec
                .builder(propertyType, propertyName)
                .addAnnotation(JsProperty.class)
                .build());
            alreadyDone.add(propertyName);
        });

        TypeElement superClass =
            (TypeElement) ((DeclaredType) component.getSuperclass()).asElement();

        // Stop when we reach VueComponent
        if (VueComponent.class.getCanonicalName().equals(superClass.getQualifiedName().toString()))
            return;

        addComputedAttributes(superClass, componentWithTemplateBuilder, alreadyDone);
    }

    /**
     * Process watchers from the Component Class.
     * @param component {@link VueComponent} to process
     * @param optionsBuilder A {@link MethodSpec.Builder} for the method that creates the
     * {@link VueComponentOptions}
     * @param componentWithTemplateBuilder Builder for the ComponentWithTemplate class
     */
    private void processWatchers(TypeElement component, MethodSpec.Builder optionsBuilder,
        Builder componentWithTemplateBuilder)
    {
        getMethodsWithAnnotation(component, Watch.class).forEach(method -> {
            Watch watch = method.getAnnotation(Watch.class);

            optionsBuilder.addStatement("options.addJavaWatch($S, $S, $L)",
                method.getSimpleName().toString(),
                watch.propertyName(),
                watch.isDeep());

            generateProxyMethod(componentWithTemplateBuilder, method);
        });
    }

    /**
     * Process prop validators from the Component Class.
     * @param component {@link VueComponent} to process
     * @param optionsBuilder A {@link MethodSpec.Builder} for the method that creates the
     * {@link VueComponentOptions}
     * @param componentWithTemplateBuilder Builder for the ComponentWithTemplate class
     */
    private void processPropValidators(TypeElement component, MethodSpec.Builder optionsBuilder,
        Builder componentWithTemplateBuilder)
    {
        getMethodsWithAnnotation(component, PropValidator.class).forEach(method -> {
            PropValidator propValidator = method.getAnnotation(PropValidator.class);

            String propertyName = propValidator.propertyName();
            optionsBuilder.addStatement("options.addJavaPropValidator($S, $S)",
                method.getSimpleName().toString(),
                propertyName);

            generateProxyMethod(componentWithTemplateBuilder, method);
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
     * @param componentWithTemplateBuilder Builder for the ComponentWithTemplate class
     */
    private void processRenderFunction(TypeElement component, MethodSpec.Builder optionsBuilder,
        Builder componentWithTemplateBuilder)
    {
        if (!GenerationUtil.hasInterface(processingEnv, component.asType(), HasRender.class))
            return;

        componentWithTemplateBuilder.addMethod(MethodSpec
            .methodBuilder("render")
            .returns(VNode.class)
            .addParameter(CreateElementFunction.class, "createElementFunction")
            .addAnnotation(JsMethod.class)
            .addStatement("return super.render(new $T(createElementFunction))", VNodeBuilder.class)
            .build());

        optionsBuilder.addStatement("options.addRootJavaMethod($S)", "render");
    }

    /**
     * Generate a {@link JsMethod} proxy method for {@link VueComponent} method.
     * This proxy will keep the same name in JS and can be therefore passed to Vue to
     * configure our {@link VueComponent}.
     * @param componentWithTemplateBuilder The builder for our ComponentWithTemplate class
     * @param originalMethod Method to proxify
     */
    private void generateProxyMethod(Builder componentWithTemplateBuilder,
        ExecutableElement originalMethod)
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

        componentWithTemplateBuilder.addMethod(proxyMethodBuilder.build());
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

        return HOOKS_MAP.containsKey(methodJavaName) && GenerationUtil.hasInterface(processingEnv,
            component.asType(),
            HOOKS_MAP.get(methodJavaName));
    }

    private boolean isVisibleInJS(ExecutableElement method)
    {
        return (hasComponentJsTypeAnnotation && method.getModifiers().contains(Modifier.PUBLIC))
            || method.getAnnotation(JsMethod.class) != null;
    }

    private boolean isVisibleInJS(VariableElement variableElement)
    {
        return (hasComponentJsTypeAnnotation && variableElement
            .getModifiers()
            .contains(Modifier.PUBLIC)) || variableElement.getAnnotation(JsProperty.class) != null;
    }

    private Stream<ExecutableElement> getMethodsWithAnnotation(TypeElement component,
        Class<? extends Annotation> annotation)
    {
        return ElementFilter
            .methodsIn(component.getEnclosedElements())
            .stream()
            .filter(method -> method.getAnnotation(annotation) != null);
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
