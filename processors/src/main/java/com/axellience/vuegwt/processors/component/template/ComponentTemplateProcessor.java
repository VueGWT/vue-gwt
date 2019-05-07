package com.axellience.vuegwt.processors.component.template;

import static com.axellience.vuegwt.processors.utils.ComponentGeneratorsUtil.getComponentLocalComponents;
import static com.axellience.vuegwt.processors.utils.ComponentGeneratorsUtil.getSuperComponentType;
import static com.axellience.vuegwt.processors.utils.GeneratorsNameUtil.componentToTagName;
import static com.axellience.vuegwt.processors.utils.GeneratorsNameUtil.computedPropertyNameToFieldName;
import static com.axellience.vuegwt.processors.utils.GeneratorsUtil.getComputedPropertyName;
import static com.axellience.vuegwt.processors.utils.GeneratorsUtil.hasAnnotation;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.annotations.component.Ref;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.processors.component.ComponentExposedTypeGenerator;
import com.axellience.vuegwt.processors.component.template.builder.TemplateMethodsBuilder;
import com.axellience.vuegwt.processors.component.template.parser.StyleParser;
import com.axellience.vuegwt.processors.component.template.parser.TemplateParser;
import com.axellience.vuegwt.processors.component.template.parser.context.StyleParserContext;
import com.axellience.vuegwt.processors.component.template.parser.context.TemplateParserContext;
import com.axellience.vuegwt.processors.component.template.parser.context.localcomponents.LocalComponent;
import com.axellience.vuegwt.processors.component.template.parser.context.localcomponents.LocalComponents;
import com.axellience.vuegwt.processors.component.template.parser.refs.RefFieldValidator;
import com.axellience.vuegwt.processors.component.template.parser.refs.RefInfo;
import com.axellience.vuegwt.processors.component.template.parser.result.StyleParserResult;
import com.axellience.vuegwt.processors.component.template.parser.result.TemplateParserResult;
import com.axellience.vuegwt.processors.utils.ComponentGeneratorsUtil;
import com.axellience.vuegwt.processors.utils.MissingComponentAnnotationException;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec.Builder;
import com.sun.tools.javac.util.ClientCodeException;
import com.sun.tools.javac.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * Process the HTML template for a given {@link IsVueComponent}.
 *
 * @author Adrien Baron
 */
public class ComponentTemplateProcessor {

  private final ProcessingEnvironment processingEnvironment;
  private final Filer filer;
  private final Messager messager;
  private final Elements elements;

  public ComponentTemplateProcessor(ProcessingEnvironment processingEnvironment) {
    this.processingEnvironment = processingEnvironment;
    filer = processingEnvironment.getFiler();
    messager = processingEnvironment.getMessager();
    elements = processingEnvironment.getElementUtils();
  }

  public void processComponentTemplate(TypeElement componentTypeElement,
      ComponentExposedTypeGenerator exposedTypeGenerator) {
    ClassName componentTypeName = ClassName.get(componentTypeElement);
    Optional<TemplateFileResource> optionalTemplateContent = 
        getTemplateContent(componentTypeName, componentTypeElement);

    if (!optionalTemplateContent.isPresent()) {
      return;
    }

    LocalComponents localComponents = new LocalComponents();
    findLocalComponentsForComponent(localComponents, componentTypeElement);

    // Parse the styles
    StringBuilder sb = new StringBuilder();
    StyleParserContext styleContext = new StyleParserContext(componentTypeElement);
    tryParseStyle(componentTypeName, componentTypeElement, sb, styleContext, "scss");
    tryParseStyle(componentTypeName, componentTypeElement, sb, styleContext, "css");

    // Initialize the template parser context based on the VueComponent type element
    TemplateParserContext templateParserContext =
        new TemplateParserContext(componentTypeElement, localComponents);
    registerFieldsAndMethodsInContext(templateParserContext,
        componentTypeElement,
        new HashSet<>(),
        new HashSet<>());

    // Forward mandatory attributes to template context
     templateParserContext.getMandatoryAttributes().putAll(styleContext.getMandatoryAttributes());

    // Parse the template
    TemplateParserResult templateParserResult = new TemplateParser().parseHtmlTemplate(
        optionalTemplateContent.get().content,
        templateParserContext,
        elements,
        messager,
        optionalTemplateContent.get().uri);
    
    validateRefs(templateParserResult.getRefs(), componentTypeElement);

    Optional.ofNullable(templateParserResult.getScopedCss()).ifPresent(sb::append);
    
    registerScopedCss(exposedTypeGenerator.getClassBuilder(), sb.toString());

    // Add expressions from the template to ExposedType and compile template
    TemplateMethodsBuilder templateMethodsBuilder = new TemplateMethodsBuilder();
    templateMethodsBuilder.addTemplateMethodsToComponentExposedType(exposedTypeGenerator,
        templateParserResult);
  }

  private void tryParseStyle(ClassName componentTypeName, TypeElement componentTypeElement, StringBuilder sb,
      StyleParserContext styleContext, String type) {
    Optional<TemplateFileResource> optionalStyleContent = getStyleContent(componentTypeName, componentTypeElement, type);
    if (optionalStyleContent.isPresent()) {
      StyleParserResult styleParserResult = new StyleParser().parseStyleTemplate(
          type,
          optionalStyleContent.get().content,
          styleContext,
          messager,
          optionalStyleContent.get().uri);
      
      Optional.ofNullable(styleParserResult.getScopedCss()).ifPresent(sb::append);
    }
  }

  private Optional<TemplateFileResource> getStyleContent(ClassName componentTypeName, TypeElement componentTypeElement,
      String type) {
    String path = slashify(componentTypeName.reflectionName()) + "." + type;
    Optional<TemplateFileResource> result = getTemplateContentAtLocation(path,
        StandardLocation.CLASS_OUTPUT);
    if (result.isPresent()) {
      return result;
    }

    result = getTemplateContentAtLocation(path, StandardLocation.CLASS_PATH);
    if (result.isPresent()) {
      return result;
    }
    
    return Optional.empty();
  }

  /**
   * Process the ComponentExposedType class to register all the fields and methods visible in the
   * context. TODO: Improve this method by putting things together with {@link
   * ComponentExposedTypeGenerator}
   *
   * @param componentTypeElement The class to process
   */
  private void registerFieldsAndMethodsInContext(TemplateParserContext templateParserContext,
      TypeElement componentTypeElement, Set<String> alreadyDoneVariable,
      Set<String> alreadyDoneMethods) {
    ElementFilter
        .fieldsIn(componentTypeElement.getEnclosedElements())
        .stream()
        .filter(field -> field.getAnnotation(Data.class) != null
            || field.getAnnotation(Prop.class) != null)
        .forEach(field -> {
          String name = field.getSimpleName().toString();
          if (alreadyDoneVariable.contains(name)) {
            return;
          }

          alreadyDoneVariable.add(name);
          templateParserContext.addRootVariable(ClassName.get(field.asType()), name);
        });

    ElementFilter
        .methodsIn(componentTypeElement.getEnclosedElements())
        .stream()
        .filter(method -> hasAnnotation(method, Computed.class))
        .filter(method -> !"void".equals(method.getReturnType().toString()))
        .forEach(method -> {
          String name = getComputedPropertyName(method);
          if (alreadyDoneVariable.contains(name)) {
            return;
          }
          alreadyDoneVariable.add(name);

          TypeMirror propertyType;
          if ("void".equals(method.getReturnType().toString())) {
            propertyType = method.getParameters().get(0).asType();
          } else {
            propertyType = method.getReturnType();
          }

          templateParserContext
              .addRootComputedProperty(ClassName.get(propertyType), name,
                  computedPropertyNameToFieldName(name));
        });

    ElementFilter
        .methodsIn(componentTypeElement.getEnclosedElements())
        .stream()
        .filter(ComponentGeneratorsUtil::isMethodVisibleInTemplate)
        .map(ExecutableElement::getSimpleName)
        .map(Object::toString)
        .forEach(methodName -> {
          if (alreadyDoneMethods.contains(methodName)) {
            return;
          }
          alreadyDoneMethods.add(methodName);

          templateParserContext.addRootMethod(methodName);
        });

    getSuperComponentType(componentTypeElement)
        .ifPresent(superComponent -> registerFieldsAndMethodsInContext(
            templateParserContext,
            superComponent,
            alreadyDoneVariable,
            alreadyDoneMethods));
  }

  /**
   * Register all locally declared components.
   *
   * @param localComponents The {@link LocalComponents} where we register our local components
   * @param componentTypeElement The class to process
   */
  private void findLocalComponentsForComponent(LocalComponents localComponents,
      TypeElement componentTypeElement) {
    Component componentAnnotation = componentTypeElement.getAnnotation(Component.class);
    if (componentAnnotation == null) {
      return;
    }

    processLocalComponentClass(localComponents, componentTypeElement);
    getComponentLocalComponents(elements, componentTypeElement)
        .stream()
        .map(DeclaredType.class::cast)
        .map(DeclaredType::asElement)
        .map(TypeElement.class::cast)
        .forEach(childTypeElement -> processLocalComponentClass(localComponents,
            childTypeElement));

    getSuperComponentType(componentTypeElement)
        .ifPresent(superComponentType -> findLocalComponentsForComponent(
            localComponents,
            superComponentType));
  }

  /**
   * Register the local component and all of its {@link Prop}. This will be used for type
   * validation.
   *
   * @param localComponents The {@link LocalComponents} object where we should register our {@link
   * LocalComponent}
   * @param localComponentType The class to process
   */
  private void processLocalComponentClass(LocalComponents localComponents,
      TypeElement localComponentType) {

    String localComponentTagName;
    try {
      localComponentTagName = componentToTagName(localComponentType);
    } catch (MissingComponentAnnotationException e) {
      e.printStackTrace();
      messager.printMessage(Kind.ERROR,
          "Missing @Component or @JsComponent annotation on imported component: "
              + localComponentType.toString(), localComponentType);
      return;
    }

    if (localComponents.hasLocalComponent(localComponentTagName)) {
      return;
    }

    LocalComponent localComponent = localComponents
        .addLocalComponent(localComponentTagName, localComponentType.asType());

    ElementFilter.fieldsIn(localComponentType.getEnclosedElements()).forEach(field -> {
      Prop propAnnotation = field.getAnnotation(Prop.class);
      if (propAnnotation != null) {
        localComponent.addProp(field.getSimpleName().toString(),
            TypeName.get(field.asType()),
            propAnnotation.required());
      }
    });
  }

  private void validateRefs(Set<RefInfo> templateRefs, TypeElement component) {
    RefFieldValidator refFieldValidator = new RefFieldValidator(component, templateRefs,
        processingEnvironment);

    ElementFilter
        .fieldsIn(component.getEnclosedElements())
        .stream()
        .filter(field -> hasAnnotation(field, Ref.class))
        .forEach(refFieldValidator::validateRefField);
  }

  private static class TemplateFileResource {

    public final String content;
    public final URI uri;

    public TemplateFileResource(String content, URI uri) {
      this.content = content;
      this.uri = uri;
    }
  }

  private Optional<TemplateFileResource> getTemplateContent(ClassName componentTypeName,
      TypeElement componentTypeElement) {
    String path = slashify(componentTypeName.reflectionName()) + ".html";
    Optional<TemplateFileResource> result = getTemplateContentAtLocation(path,
        StandardLocation.CLASS_OUTPUT);
    if (result.isPresent()) {
      return result;
    }

    result = getTemplateContentAtLocation(path, StandardLocation.CLASS_PATH);
    if (result.isPresent()) {
      return result;
    }

    messager.printMessage(Kind.ERROR,
        "Couldn't find template for component: "
            + componentTypeName.simpleName()
            + ". Make sure you included src/main/java in your Resources. Check our setup guide for help.",
        componentTypeElement);
    return Optional.empty();
  }

  private Optional<TemplateFileResource> getTemplateContentAtLocation(String path,
      StandardLocation location) {
    FileObject resource;
    try {
      resource = filer.getResource(location, "", path);
    } catch (IOException ignore) {
      return Optional.empty();
    } catch (RuntimeException e) {
      return Optional.empty();
    }

    // Get template content from HTML file
    try {
      return Optional
          .of(new TemplateFileResource(resource.getCharContent(true).toString(), resource.toUri()));
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  private static void registerScopedCss(Builder componentExposedTypeBuilder,
      String scopedCss) {
    componentExposedTypeBuilder.addMethod(MethodSpec
        .methodBuilder("getScopedCss")
        .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
        .returns(ParameterizedTypeName.get(String.class))
        .addStatement("return $S", scopedCss)
        .build());
  }

  private static String slashify(String s) {
    return s.replace(".", "/").replace("$", ".");
  }
}
