package com.axellience.vuegwt.processors.component;

import static com.axellience.vuegwt.processors.utils.ComponentGeneratorsUtil.getSuperComponentCount;
import static com.axellience.vuegwt.processors.utils.ComponentGeneratorsUtil.getSuperComponentType;
import static com.axellience.vuegwt.processors.utils.ComponentGeneratorsUtil.hasTemplate;
import static com.axellience.vuegwt.processors.utils.ComponentGeneratorsUtil.isMethodVisibleInJS;
import static com.axellience.vuegwt.processors.utils.GeneratorsNameUtil.componentExposedTypeName;
import static com.axellience.vuegwt.processors.utils.GeneratorsNameUtil.componentInjectedDependenciesName;
import static com.axellience.vuegwt.processors.utils.GeneratorsNameUtil.methodToEventName;
import static com.axellience.vuegwt.processors.utils.GeneratorsUtil.getFieldMarkingValueForType;
import static com.axellience.vuegwt.processors.utils.GeneratorsUtil.getUnusableByJSAnnotation;
import static com.axellience.vuegwt.processors.utils.GeneratorsUtil.hasAnnotation;
import static com.axellience.vuegwt.processors.utils.GeneratorsUtil.hasInterface;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.annotations.component.Emit;
import com.axellience.vuegwt.core.annotations.component.HookMethod;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.annotations.component.PropDefault;
import com.axellience.vuegwt.core.annotations.component.PropValidator;
import com.axellience.vuegwt.core.annotations.component.Watch;
import com.axellience.vuegwt.core.client.VueGWT;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import com.axellience.vuegwt.core.client.component.hooks.HasRender;
import com.axellience.vuegwt.core.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.core.client.component.options.computed.ComputedKind;
import com.axellience.vuegwt.core.client.component.options.watch.WatchOptions;
import com.axellience.vuegwt.core.client.tools.FieldsExposer;
import com.axellience.vuegwt.core.client.tools.VueGWTTools;
import com.axellience.vuegwt.core.client.vnode.VNode;
import com.axellience.vuegwt.core.client.vnode.builder.CreateElementFunction;
import com.axellience.vuegwt.core.client.vnode.builder.VNodeBuilder;
import com.axellience.vuegwt.core.client.vue.VueJsConstructor;
import com.axellience.vuegwt.processors.component.template.ComponentTemplateProcessor;
import com.axellience.vuegwt.processors.component.validators.CollectionFieldsValidator;
import com.axellience.vuegwt.processors.utils.ComponentGeneratorsUtil;
import com.axellience.vuegwt.processors.utils.GeneratorsUtil;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import elemental2.core.Function;
import elemental2.core.JsArray;
import java.lang.annotation.Annotation;
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
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.JsPropertyMap;

/**
 * Generate a ExposedType wrapper for the user Java {@link IsVueComponent}. It will wrap any non
 * JsInterop methods from the original component to make them visible to JS. It also provides the
 * {@link VueComponentOptions} that will be passed down to Vue.js to initialize our {@link
 * VueJsConstructor}.
 *
 * @author Adrien Baron
 */
public class ComponentExposedTypeGenerator {

  private static final String METHOD_IN_PROTO_PREFIX = "vg$e";

  private final ProcessingEnvironment processingEnv;
  private final Filer filer;
  private final Messager messager;
  private final Elements elements;
  private final ComponentTemplateProcessor componentTemplateProcessor;
  private final CollectionFieldsValidator collectionFieldsValidator;

  private TypeElement component;
  private Builder componentExposedTypeBuilder;
  private MethodSpec.Builder optionsBuilder;
  private Builder protoClassBuilder;
  private Set<VariableElement> fieldsToMarkAsData;
  private Set<ExposedField> fieldsWithNameExposed;
  private int methodsInProtoCount;

  public ComponentExposedTypeGenerator(ProcessingEnvironment processingEnvironment) {
    processingEnv = processingEnvironment;
    filer = processingEnvironment.getFiler();
    messager = processingEnvironment.getMessager();
    elements = processingEnvironment.getElementUtils();
    componentTemplateProcessor = new ComponentTemplateProcessor(processingEnvironment);
    collectionFieldsValidator = new CollectionFieldsValidator(processingEnvironment.getTypeUtils(),
        elements,
        messager);
  }

  public void generate(TypeElement component,
      ComponentInjectedDependenciesBuilder dependenciesBuilder) {
    // Template resource abstract class
    ClassName componentWithSuffixClassName = componentExposedTypeName(component);

    this.component = component;
    methodsInProtoCount = 0;
    fieldsToMarkAsData = new HashSet<>();
    fieldsWithNameExposed = new HashSet<>();
    componentExposedTypeBuilder = createComponentExposedTypeBuilder(componentWithSuffixClassName);
    protoClassBuilder = createProtoClassBuilder();

    optionsBuilder = createOptionsMethodBuilder();
    Set<ExecutableElement> hookMethodsFromInterfaces = getHookMethodsFromInterfaces();
    processData();
    processProps();
    processComputed();
    processPropValidators();
    processPropDefaultValues();
    processHooks(hookMethodsFromInterfaces);
    processMethods(hookMethodsFromInterfaces);
    processEmitMethods();
    processRenderFunction();
    createCreatedHook(dependenciesBuilder);
    initComponentDataFields();

    // Process the HTML template if there is one
    if (hasTemplate(processingEnv, component)) {
      componentTemplateProcessor.processComponentTemplate(component, this);
      optionsBuilder.addStatement(
          "options.initRenderFunctions(getRenderFunction(), getStaticRenderFunctions())");
    }

    // Finish building Options getter
    optionsBuilder.addStatement("return options");
    componentExposedTypeBuilder.addMethod(optionsBuilder.build());

    // Finish building proto
    componentExposedTypeBuilder.addType(protoClassBuilder.build());

    // Expose all fields whose names we determine at runtime to JS
    exposeExposedFieldsToJs();

    // And generate our Java Class
    GeneratorsUtil.toJavaFile(filer,
        componentExposedTypeBuilder,
        componentWithSuffixClassName,
        component);
  }

  /**
   * Create and return the builder for the ExposedType of our {@link IsVueComponent}.
   *
   * @param exposedTypeClassName The name of the generated ExposedType class
   * @return A Builder to build the class
   */
  private Builder createComponentExposedTypeBuilder(ClassName exposedTypeClassName) {
    return TypeSpec
        .classBuilder(exposedTypeClassName)
        .addModifiers(Modifier.PUBLIC)
        .superclass(TypeName.get(component.asType()))
        .addAnnotation(AnnotationSpec
            .builder(Generated.class)
            .addMember("value", "$S", ComponentExposedTypeGenerator.class.getCanonicalName())
            .addMember("comments", "$S", "https://github.com/Axellience/vue-gwt")
            .build());
  }

  /**
   * Create and return the builder for the Proto of our {@link IsVueComponent}.
   *
   * @return A Builder to build the Proto class
   */
  private Builder createProtoClassBuilder() {
    componentExposedTypeBuilder.addField(
        FieldSpec
            .builder(ClassName.bestGuess("Proto"), "__proto__", Modifier.PUBLIC)
            .addAnnotation(JsProperty.class)
            .build()
    );

    return TypeSpec
        .classBuilder("Proto")
        .addSuperinterface(ParameterizedTypeName.get(JsPropertyMap.class, Object.class))
        .addModifiers(Modifier.STATIC)
        .addModifiers(Modifier.PRIVATE)
        .addAnnotation(AnnotationSpec
            .builder(JsType.class)
            .addMember("isNative", "$L", true)
            .addMember("namespace", "$T.GLOBAL", JsPackage.class)
            .addMember("name", "$S", "Object")
            .build());
  }

  /**
   * Create and return the builder for the method that creating the {@link VueComponentOptions} for
   * this {@link IsVueComponent}.
   *
   * @return A {@link MethodSpec.Builder} for the method that creates the {@link
   * VueComponentOptions}
   */
  private MethodSpec.Builder createOptionsMethodBuilder() {
    TypeName optionsTypeName =
        ParameterizedTypeName.get(ClassName.get(VueComponentOptions.class),
            ClassName.get(component));

    MethodSpec.Builder optionsMethodBuilder = MethodSpec
        .methodBuilder("getOptions")
        .addModifiers(Modifier.PUBLIC)
        .returns(optionsTypeName)
        .addStatement("$T options = new $T()", optionsTypeName, optionsTypeName)
        .addStatement("Proto p = __proto__");

    Component annotation = component.getAnnotation(Component.class);

    if (!"".equals(annotation.name())) {
      optionsMethodBuilder.addStatement("options.setName($S)", annotation.name());
    }

    optionsMethodBuilder.addStatement(
        "options.setComponentExportedTypePrototype(p)",
        VueGWT.class,
        component);

    return optionsMethodBuilder;
  }

  /**
   * Process data fields from the {@link IsVueComponent} Class.
   */
  private void processData() {
    List<VariableElement> dataFields = ElementFilter
        .fieldsIn(component.getEnclosedElements())
        .stream()
        .filter(field -> field.getAnnotation(Data.class) != null)
        .collect(Collectors.toList());

    if (dataFields.isEmpty()) {
      return;
    }

    dataFields.forEach(collectionFieldsValidator::validateComponentDataField);

    this.fieldsToMarkAsData.addAll(dataFields);
  }

  /**
   * Create
   */
  private void initComponentDataFields() {
    Component annotation = component.getAnnotation(Component.class);
    optionsBuilder.beginControlFlow("options.initData($L, $T.getFieldsName(this, () ->",
        annotation.useFactory(), VueGWTTools.class);

    fieldsToMarkAsData.forEach(field ->
        optionsBuilder.addStatement("this.$L = $L", field.getSimpleName().toString(),
            getFieldMarkingValueForType(field.asType()))
    );
    optionsBuilder.endControlFlow("))");

    fieldsWithNameExposed.addAll(
        fieldsToMarkAsData.stream()
            .map(field -> new ExposedField(field.getSimpleName().toString(), field.asType()))
            .collect(Collectors.toSet())
    );
  }

  /**
   * Generate a method that use all the fields we want to determine the name of at runtime. This is
   * to avoid GWT optimizing away assignation on those fields.
   */
  private void exposeExposedFieldsToJs() {
    if (fieldsWithNameExposed.isEmpty()) {
      return;
    }

    MethodSpec.Builder exposeFieldMethod = MethodSpec
        .methodBuilder("vg$ef")
        .addAnnotation(JsMethod.class);

    fieldsWithNameExposed
        .forEach(field -> exposeFieldMethod
            .addStatement("this.$L = $T.v()",
                field.getName(),
                FieldsExposer.class)
        );

    exposeFieldMethod
        .addStatement(
            "$T.e($L)",
            FieldsExposer.class,
            String.join(
                ",",
                fieldsWithNameExposed.stream()
                    .map(ExposedField::getName)
                    .collect(Collectors.toList())
            )
        );
    componentExposedTypeBuilder.addMethod(exposeFieldMethod.build());
  }

  /**
   * Process Vue Props from the {@link IsVueComponent} Class.
   */
  private void processProps() {
    ElementFilter
        .fieldsIn(component.getEnclosedElements())
        .stream()
        .filter(field -> hasAnnotation(field, Prop.class))
        .forEach(field -> {
          String propName = field.getSimpleName().toString();
          Prop prop = field.getAnnotation(Prop.class);

          collectionFieldsValidator.validateComponentPropField(field);

          fieldsWithNameExposed.add(new ExposedField(propName, field.asType()));

          optionsBuilder.addStatement(
              "options.addJavaProp($S, $T.getFieldName(this, () -> this.$L = $L), $L, $S)",
              propName,
              VueGWTTools.class,
              propName,
              getFieldMarkingValueForType(field.asType()),
              prop.required(),
              prop.checkType() ? getNativeNameForJavaType(field.asType()) : null
          );
        });
  }

  /**
   * Process computed properties from the Component Class.
   */
  private void processComputed() {
    getMethodsWithAnnotation(component, Computed.class).forEach(method -> {
      ComputedKind kind = ComputedKind.GETTER;
      if ("void".equals(method.getReturnType().toString())) {
        kind = ComputedKind.SETTER;
      }

      String exposedMethodName = exposeExistingJavaMethodToJs(method);
      String propertyName = GeneratorsUtil.getComputedPropertyName(method);
      TypeMirror propertyType = getComputedPropertyTypeFromMethod(method);
      fieldsWithNameExposed.add(new ExposedField(propertyName, propertyType));
      optionsBuilder.addStatement(
          "options.addJavaComputed(p.$L, $T.getFieldName(this, () -> this.$L = $L), $T.$L)",
          exposedMethodName,
          VueGWTTools.class,
          propertyName,
          getFieldMarkingValueForType(propertyType),
          ComputedKind.class,
          kind
      );
    });

    addFieldsForComputedMethod(component, new HashSet<>());
  }

  /**
   * Process template methods for our {@link IsVueComponent} class.
   *
   * @param hookMethodsFromInterfaces Hook methods from the interface the {@link IsVueComponent}
   * implements
   */
  private void processMethods(Set<ExecutableElement> hookMethodsFromInterfaces) {
    List<ExecutableElement> templateMethods = ElementFilter
        .methodsIn(component.getEnclosedElements())
        .stream()
        .filter(ComponentGeneratorsUtil::isMethodVisibleInTemplate)
        .filter(method -> !isHookMethod(component, method, hookMethodsFromInterfaces))
        .collect(Collectors.toList());

    templateMethods.forEach(method -> {
      String methodName = method.getSimpleName().toString();
      String exposedMethodName = exposeExistingJavaMethodToJs(method);
      optionsBuilder.addStatement("options.addMethod($S, p.$L)", methodName, exposedMethodName);
    });
  }

  /**
   * Process @Emit methods with no @JsMethod annotation
   */
  private void processEmitMethods() {
    ElementFilter
        .methodsIn(component.getEnclosedElements())
        .stream()
        .filter(method -> hasAnnotation(method, Emit.class))
        .filter(method -> !hasAnnotation(method, JsMethod.class))
        .forEach(this::exposeExistingJavaMethodToJs);
  }

  /**
   * Add fields for computed methods so they are visible in the template
   *
   * @param component Component we are currently processing
   * @param alreadyDone Already processed computed properties (in case there is a getter and a
   */
  private void addFieldsForComputedMethod(TypeElement component, Set<String> alreadyDone) {
    getMethodsWithAnnotation(component, Computed.class).forEach(method -> {
      String propertyName = GeneratorsUtil.getComputedPropertyName(method);

      if (alreadyDone.contains(propertyName)) {
        return;
      }

      TypeMirror propertyType = getComputedPropertyTypeFromMethod(method);
      componentExposedTypeBuilder.addField(TypeName.get(propertyType),
          propertyName,
          Modifier.PROTECTED);
      alreadyDone.add(propertyName);
    });

    getSuperComponentType(component)
        .ifPresent(superComponent -> addFieldsForComputedMethod(superComponent, alreadyDone));
  }

  private TypeMirror getComputedPropertyTypeFromMethod(ExecutableElement method) {
    TypeMirror propertyType;
    if ("void".equals(method.getReturnType().toString())) {
      propertyType = method.getParameters().get(0).asType();
    } else {
      propertyType = method.getReturnType();
    }
    return propertyType;
  }

  /**
   * Process watchers from the Component Class.
   *
   * @param createdMethodBuilder Builder for the created hook method
   */
  private void processWatchers(MethodSpec.Builder createdMethodBuilder) {
    createdMethodBuilder.addStatement("Proto p = __proto__");
    getMethodsWithAnnotation(component, Watch.class)
        .forEach(method -> processWatcher(createdMethodBuilder, method));
  }

  /**
   * Process a watcher from the Component Class.
   *
   * @param createdMethodBuilder Builder for the created hook method
   * @param method The method we are currently processing
   */
  private void processWatcher(MethodSpec.Builder createdMethodBuilder, ExecutableElement method) {
    Watch watch = method.getAnnotation(Watch.class);

    String exposedMethodName = exposeExistingJavaMethodToJs(method);
    String watcherTriggerMethodName = addNewMethodToProto();

    MethodSpec.Builder watcherMethodBuilder = MethodSpec
        .methodBuilder(watcherTriggerMethodName)
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(JsMethod.class)
        .returns(Object.class);

    String[] valueSplit = watch.value().split("\\.");

    String currentExpression = "";
    for (int i = 0; i < valueSplit.length - 1; i++) {
      currentExpression += valueSplit[i];
      watcherMethodBuilder.addStatement("if ($L == null) return null", currentExpression);
      currentExpression += ".";
    }

    watcherMethodBuilder.addStatement("return $L", watch.value());

    componentExposedTypeBuilder.addMethod(watcherMethodBuilder.build());

    createdMethodBuilder
        .addStatement("vue().$L(p.$L, p.$L, $T.of($L, $L))", "$watch",
            watcherTriggerMethodName, exposedMethodName, WatchOptions.class, watch.isDeep(),
            watch.isImmediate());
  }

  /**
   * Process prop validators from the Component Class.
   */
  private void processPropValidators() {
    getMethodsWithAnnotation(component, PropValidator.class).forEach(method -> {
      PropValidator propValidator = method.getAnnotation(PropValidator.class);

      if (!TypeName.get(method.getReturnType()).equals(TypeName.BOOLEAN)) {
        printError("Method "
            + method.getSimpleName()
            + " annotated with PropValidator must return a boolean.", component);
      }

      String exposedMethodName = exposeExistingJavaMethodToJs(method);
      String propertyName = propValidator.value();
      optionsBuilder.addStatement("options.addJavaPropValidator(p.$L, $S)",
          exposedMethodName,
          propertyName);
    });
  }

  /**
   * Process prop default values from the Component Class.
   */
  private void processPropDefaultValues() {
    getMethodsWithAnnotation(component, PropDefault.class).forEach(method -> {
      PropDefault propValidator = method.getAnnotation(PropDefault.class);

      String exposedMethodName = exposeExistingJavaMethodToJs(method);
      String propertyName = propValidator.value();
      optionsBuilder.addStatement("options.addJavaPropDefaultValue(p.$L, $S)",
          exposedMethodName,
          propertyName);
    });
  }

  /**
   * Process hook methods from the Component Class.
   *
   * @param hookMethodsFromInterfaces Hook methods from the interface the {@link IsVueComponent}
   * implements
   */
  private void processHooks(Set<ExecutableElement> hookMethodsFromInterfaces) {
    ElementFilter
        .methodsIn(component.getEnclosedElements())
        .stream()
        .filter(method -> isHookMethod(component, method, hookMethodsFromInterfaces))
        // Created hook is already added by createCreatedHook
        .filter(method -> !"created".equals(method.getSimpleName().toString()))
        .forEach(method -> {
          String exposedMethodName = exposeExistingJavaMethodToJs(method);
          String methodName = method.getSimpleName().toString();
          optionsBuilder.addStatement("options.addHookMethod($S, p.$L)", methodName, exposedMethodName);
        });
  }

  /**
   * Return all hook methods from the implemented interfaces
   *
   * @return Hook methods that must be overridden in the Component
   */
  private Set<ExecutableElement> getHookMethodsFromInterfaces() {
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

  private void validateHookMethod(ExecutableElement hookMethod, TypeElement component) {
    if (!isMethodVisibleInJS(hookMethod)) {
      printError("Method "
          + hookMethod.getSimpleName()
          + " annotated with HookMethod should also have @JsMethod property.", component);
    }
  }

  /**
   * Process the render function from the Component Class if it has one.
   */
  private void processRenderFunction() {
    if (!hasInterface(processingEnv, component.asType(), HasRender.class)) {
      return;
    }

    componentExposedTypeBuilder.addMethod(MethodSpec
        .methodBuilder("vg$render")
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(JsMethod.class)
        .returns(VNode.class)
        .addParameter(CreateElementFunction.class, "createElementFunction")
        .addStatement("return super.render(new $T(createElementFunction))", VNodeBuilder.class)
        .build());

    addMethodToProto("vg$render");

    // Register the render method
    optionsBuilder.addStatement("options.addHookMethod($S, p.$L)", "render", "vg$render");
  }

  /**
   * Create the "created" hook method. This method will be called on each Component when it's
   * created. It will inject dependencies if any.
   *
   * @param dependenciesBuilder Builder for our component dependencies, needed here to inject the
   * dependencies in the instance
   */
  private void createCreatedHook(ComponentInjectedDependenciesBuilder dependenciesBuilder) {
    String hasRunCreatedFlagName = "vg$hrc_" + getSuperComponentCount(component);
    componentExposedTypeBuilder
        .addField(
            FieldSpec.builder(boolean.class, hasRunCreatedFlagName, Modifier.PUBLIC)
                .addAnnotation(JsProperty.class)
                .build()
        );

    MethodSpec.Builder createdMethodBuilder =
        MethodSpec.methodBuilder("vg$created")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(JsMethod.class);

    // Avoid infinite recursion in case calling the Java constructor calls Vue.js constructor
    // This can happen when extending an existing JS component
    createdMethodBuilder
        .addStatement("if ($L) return", hasRunCreatedFlagName)
        .addStatement("$L = true", hasRunCreatedFlagName);

    createdMethodBuilder
        .addStatement("vue().$L().proxyFields(this)", "$options");
    injectDependencies(component, dependenciesBuilder, createdMethodBuilder);
    initFieldsValues(component, createdMethodBuilder);

    processWatchers(createdMethodBuilder);

    if (hasInterface(processingEnv, component.asType(), HasCreated.class)) {
      createdMethodBuilder.addStatement("super.created()");
    }

    componentExposedTypeBuilder.addMethod(createdMethodBuilder.build());

    // Register the hook
    addMethodToProto("vg$created");
    optionsBuilder.addStatement("options.addHookMethod($S, p.$L)", "created", "vg$created");
  }

  /**
   * Inject the dependencies in the instance if needed. We do that by injecting an instance of an
   * object generated by {@link ComponentInjectedDependenciesBuilder}. We then copy the fields of
   * this object, and call methods that needs injection.
   *
   * @param component {@link IsVueComponent} to process
   * @param dependenciesBuilder Builder for our component dependencies, needed here to inject the
   * dependencies in the instance
   * @param createdMethodBuilder Builder for our Create method
   */
  private void injectDependencies(TypeElement component,
      ComponentInjectedDependenciesBuilder dependenciesBuilder,
      MethodSpec.Builder createdMethodBuilder) {
    if (!dependenciesBuilder.hasInjectedDependencies()) {
      return;
    }

    createDependenciesInstance(component, createdMethodBuilder);
    copyDependenciesFields(dependenciesBuilder, createdMethodBuilder);
    callMethodsWithDependencies(dependenciesBuilder, createdMethodBuilder);
  }

  private void createDependenciesInstance(TypeElement component,
      MethodSpec.Builder createdMethodBuilder) {
    ClassName dependenciesName = componentInjectedDependenciesName(component);
    createdMethodBuilder.addStatement(
        "$T dependencies = ($T) vue().$L.getProvider($T.class).get()",
        dependenciesName,
        dependenciesName,
        "$options()",
        component);
  }

  private void copyDependenciesFields(ComponentInjectedDependenciesBuilder dependenciesBuilder,
      MethodSpec.Builder createdMethodBuilder) {
    dependenciesBuilder
        .getInjectedFieldsName()
        .forEach(fieldName -> createdMethodBuilder.addStatement("super.$L = dependencies.$L",
            fieldName,
            fieldName));
  }

  private void callMethodsWithDependencies(
      ComponentInjectedDependenciesBuilder dependenciesBuilder,
      MethodSpec.Builder createdMethodBuilder) {
    for (Entry<String, List<String>> methodNameParametersEntry : dependenciesBuilder
        .getInjectedParametersByMethod()
        .entrySet()) {
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
   * Init fields at creation by using an instance of the Java class
   *
   * @param component {@link IsVueComponent} to process
   * @param createdMethodBuilder Builder for our Create method
   */
  private void initFieldsValues(TypeElement component, MethodSpec.Builder createdMethodBuilder) {
    // Do not init instance fields for abstract components
    if (component.getModifiers().contains(Modifier.ABSTRACT)) {
      return;
    }

    createdMethodBuilder.addStatement(
        "$T.initComponentInstanceFields(this, new $T())",
        VueGWTTools.class,
        component);
  }

  /**
   * Generate a JsInterop proxy method for a {@link IsVueComponent} method. This proxy will keep the
   * same name in JS and can be therefore passed to Vue to configure our {@link IsVueComponent}.
   *
   * @param originalMethod Method to proxify
   * @return The exposed method name
   */
  private String exposeExistingJavaMethodToJs(ExecutableElement originalMethod) {
    Emit emitAnnotation = originalMethod.getAnnotation(Emit.class);

    if (emitAnnotation != null) {
      // @Emit methods must be overridden so the exposed method must keep the same name
      String methodName = originalMethod.getSimpleName().toString();
      createProxyJsMethod(componentExposedTypeBuilder, originalMethod, methodName);
      addMethodToProto(methodName);
      return methodName;
    } else if (!isMethodVisibleInJS(originalMethod)) {
      String proxyMethodName = addNewMethodToProto();
      createProxyJsMethod(componentExposedTypeBuilder, originalMethod, proxyMethodName);
      return proxyMethodName;
    } else {
      String methodName = originalMethod.getSimpleName().toString();
      addMethodToProto(methodName);
      return methodName;
    }
  }

  /**
   * Add a method to the ExposedType proto. This allows referencing them as this.__proto__.myMethod
   * to pass them to Vue.js. This way of referencing works both in GWT2 and Closure. The method MUST
   * have the @JsMethod annotation for this to work.
   *
   * @param methodName Name of the method to add to the proto
   */
  public void addMethodToProto(String methodName) {
    protoClassBuilder.addField(FieldSpec
        .builder(Function.class, methodName, Modifier.PUBLIC)
        .build());
  }

  /**
   * Add a method to the ExposedType proto. This allows referencing them as this.__proto__.myMethod
   * to pass them to Vue.js. This way of referencing works both in GWT2 and Closure. The method MUST
   * have the @JsMethod annotation for this to work.
   */
  private String addNewMethodToProto() {
    String methodName = METHOD_IN_PROTO_PREFIX + methodsInProtoCount;
    addMethodToProto(methodName);
    methodsInProtoCount++;

    return methodName;
  }

  /**
   * Generate a JsInterop proxy method for a {@link IsVueComponent} method.
   *
   * @param componentExposedTypeBuilder Builder for the ExposedType class
   * @param originalMethod Method to proxify
   * @param proxyMethodName The name of the proxy method
   */
  private void createProxyJsMethod(Builder componentExposedTypeBuilder,
      ExecutableElement originalMethod, String proxyMethodName) {
    Emit emitAnnotation = originalMethod.getAnnotation(Emit.class);

    MethodSpec.Builder proxyMethodBuilder = MethodSpec
        .methodBuilder(proxyMethodName)
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(JsMethod.class)
        .addAnnotation(getUnusableByJSAnnotation())
        .returns(ClassName.get(originalMethod.getReturnType()));

    originalMethod
        .getParameters()
        .forEach(parameter -> proxyMethodBuilder.addParameter(TypeName.get(parameter.asType()),
            parameter.getSimpleName().toString()));

    String methodCallParameters = getSuperMethodCallParameters(originalMethod);
    boolean hasReturnValue = !"void".equals(originalMethod.getReturnType().toString());
    if (hasReturnValue) {
      proxyMethodBuilder.addStatement("$T result = super.$L($L)",
          originalMethod.getReturnType(),
          originalMethod.getSimpleName().toString(),
          methodCallParameters);
    } else {
      proxyMethodBuilder.addStatement("super.$L($L)",
          originalMethod.getSimpleName().toString(),
          methodCallParameters);
    }

    if (emitAnnotation != null) {
      addEmitEventCall(originalMethod, proxyMethodBuilder, methodCallParameters);
    }

    if (hasReturnValue) {
      proxyMethodBuilder.addStatement("return result");
    }

    componentExposedTypeBuilder.addMethod(proxyMethodBuilder.build());
  }

  /**
   * Return the list of parameters name to pass to the super call on proxy methods.
   *
   * @param sourceMethod The source method
   * @return A string which is the list of parameters name joined by ", "
   */
  private String getSuperMethodCallParameters(ExecutableElement sourceMethod) {
    return sourceMethod
        .getParameters()
        .stream()
        .map(parameter -> parameter.getSimpleName().toString())
        .collect(Collectors.joining(", "));
  }

  /**
   * Add a call to emit an event at the end of the function
   *
   * @param originalMethod Method we are emitting an event for
   * @param proxyMethodBuilder Method we are building
   * @param methodCallParameters Chained parameters name of the method
   */
  private void addEmitEventCall(ExecutableElement originalMethod,
      MethodSpec.Builder proxyMethodBuilder, String methodCallParameters) {
    String methodName = "$emit";
    if (methodCallParameters != null && !"".equals(methodCallParameters)) {
      proxyMethodBuilder.addStatement("vue().$L($S, $L)",
          methodName,
          methodToEventName(originalMethod),
          methodCallParameters);
    } else {
      proxyMethodBuilder.addStatement("vue().$L($S)",
          methodName,
          methodToEventName(originalMethod));
    }
  }

  /**
   * Return true of the given method is a proxy method
   *
   * @param component {@link IsVueComponent} this method belongs to
   * @param method The java method to check
   * @param hookMethodsFromInterfaces All the hook methods in the implement interfaces
   * @return True if this method is a hook method, false otherwise
   */
  private boolean isHookMethod(TypeElement component, ExecutableElement method,
      Set<ExecutableElement> hookMethodsFromInterfaces) {
    if (hasAnnotation(method, HookMethod.class)) {
      validateHookMethod(method, component);
      return true;
    }

    for (ExecutableElement hookMethodsFromInterface : hookMethodsFromInterfaces) {
      if (elements.overrides(method, hookMethodsFromInterface, component)) {
        return true;
      }
    }

    return false;
  }

  private Stream<ExecutableElement> getMethodsWithAnnotation(TypeElement component,
      Class<? extends Annotation> annotation) {
    return ElementFilter
        .methodsIn(component.getEnclosedElements())
        .stream()
        .filter(method -> hasAnnotation(method, annotation));
  }

  /**
   * Transform a Java type name into a JavaScript type name. Takes care of primitive types.
   *
   * @param typeMirror A type to convert
   * @return A String representing the JavaScript type name
   */
  private String getNativeNameForJavaType(TypeMirror typeMirror) {
    TypeName typeName = TypeName.get(typeMirror);

    if (typeName.equals(TypeName.INT)
        || typeName.equals(TypeName.BYTE)
        || typeName.equals(TypeName.SHORT)
        || typeName.equals(TypeName.LONG)
        || typeName.equals(TypeName.FLOAT)
        || typeName.equals(TypeName.DOUBLE)) {
      return "Number";
    } else if (typeName.equals(TypeName.BOOLEAN)) {
      return "Boolean";
    } else if (typeName.equals(TypeName.get(String.class)) || typeName.equals(TypeName.CHAR)) {
      return "String";
    } else if (typeMirror.toString().startsWith(JsArray.class.getCanonicalName())) {
      return "Array";
    } else {
      return "Object";
    }
  }

  private void printError(String message, TypeElement component) {
    messager.printMessage(Kind.ERROR,
        message + " In VueComponent: " + component.getQualifiedName(), component);
  }

  public Builder getClassBuilder() {
    return componentExposedTypeBuilder;
  }

  public MethodSpec.Builder getOptionsBuilder() {
    return optionsBuilder;
  }
}
