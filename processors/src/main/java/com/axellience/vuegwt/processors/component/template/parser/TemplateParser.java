package com.axellience.vuegwt.processors.component.template.parser;

import static com.axellience.vuegwt.processors.utils.GeneratorsNameUtil.markedDataFieldToPlaceHolderField;
import static com.axellience.vuegwt.processors.utils.GeneratorsNameUtil.propNameToAttributeName;
import static com.axellience.vuegwt.processors.utils.GeneratorsUtil.boundedAttributeToAttributeName;
import static com.axellience.vuegwt.processors.utils.GeneratorsUtil.isBoundedAttribute;
import static com.axellience.vuegwt.processors.utils.GeneratorsUtil.stringTypeToTypeName;

import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.processors.component.template.parser.TemplateScopedCssParser.ScopedCssResult;
import com.axellience.vuegwt.processors.component.template.parser.context.TemplateParserContext;
import com.axellience.vuegwt.processors.component.template.parser.context.localcomponents.LocalComponent;
import com.axellience.vuegwt.processors.component.template.parser.context.localcomponents.LocalComponentProp;
import com.axellience.vuegwt.processors.component.template.parser.jericho.TemplateParserLoggerProvider;
import com.axellience.vuegwt.processors.component.template.parser.result.TemplateExpression;
import com.axellience.vuegwt.processors.component.template.parser.result.TemplateParserResult;
import com.axellience.vuegwt.processors.component.template.parser.variable.ComputedVariableInfo;
import com.axellience.vuegwt.processors.component.template.parser.variable.DestructuredPropertyInfo;
import com.axellience.vuegwt.processors.component.template.parser.variable.LocalVariableInfo;
import com.axellience.vuegwt.processors.component.template.parser.variable.VariableInfo;
import com.axellience.vuegwt.processors.dom.DOMElementsUtil;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithType;
import com.github.javaparser.ast.type.Type;
import com.squareup.javapoet.TypeName;
import io.bit3.jsass.CompilationException;
import io.bit3.jsass.Compiler;
import io.bit3.jsass.Options;
import io.bit3.jsass.Output;
import io.bit3.jsass.context.StringContext;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import jsinterop.base.Any;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Config;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.Tag;

/**
 * Parse an HTML Vue GWT template.
 * <br>
 * This parser find all the Java expression and process them. It will throw explicit exceptions if a
 * variable cannot be find in the context. It also automatically decide of the Java type of a given
 * expression depending on the context where it is used.
 *
 * @author Adrien Baron
 */
public class TemplateParser {

  private static Pattern VUE_ATTR_PATTERN = Pattern.compile("^(v-|:|@).*");
  private static Pattern VUE_MUSTACHE_PATTERN = Pattern.compile("\\{\\{.*?}}", Pattern.DOTALL);

  private JavaParser javaParser;
  private TemplateParserContext context;
  private Elements elements;
  private Messager messager;
  private TemplateParserLogger logger;
  private TemplateParserResult result;
  private URI htmlTemplateUri;

  private Attribute currentAttribute;
  private LocalComponentProp currentProp;
  private TypeName currentExpressionReturnType;
  private OutputDocument outputDocument;

  /**
   * Parse a given HTML template and return the a result object containing the expressions and a
   * transformed HTML.
   *
   * @param htmlTemplate The HTML template to process, as a String
   * @param context Context of the Component we are currently processing
   * @param messager Used to report errors in template during Annotation Processing
   * @return A {@link TemplateParserResult} containing the processed template and expressions
   */
  public TemplateParserResult parseHtmlTemplate(String htmlTemplate,
      TemplateParserContext context, Elements elements, Messager messager, URI htmlTemplateUri) {
    this.context = context;
    this.elements = elements;
    this.messager = messager;
    this.logger = new TemplateParserLogger(context, messager);
    this.htmlTemplateUri = htmlTemplateUri;
    this.javaParser = new JavaParser();

    initJerichoConfig(this.logger);

    Source source = new Source(htmlTemplate);
    outputDocument = new OutputDocument(source);

    result = new TemplateParserResult(context);
    processImports(source);
    result.setScopedCss(processScopedCss(source));
    source.getChildElements().forEach(this::processElement);

    result.setProcessedTemplate(outputDocument.toString());
    return result;
  }

  private static void initJerichoConfig(TemplateParserLogger logger) {
    // Allow as many invalid character in attributes as possible
    Attributes.setDefaultMaxErrorCount(Integer.MAX_VALUE);
    // Allow any element to be self closing
    Config.IsHTMLEmptyElementTagRecognised = true;
    // Use our own logger
    Config.LoggerProvider = new TemplateParserLoggerProvider(logger);
  }

  /**
   * Add java imports in the template to the context.
   *
   * @param doc The document to process
   */
  private void processImports(Source doc) {
    doc
        .getAllElements()
        .stream()
        .filter(element -> "vue-gwt:import".equalsIgnoreCase(element.getName()))
        .peek(importElement -> {
          String classAttributeValue = importElement.getAttributeValue("class");
          if (classAttributeValue != null) {
            context.addImport(classAttributeValue);
          }

          String staticAttributeValue = importElement.getAttributeValue("static");
          if (staticAttributeValue != null) {
            context.addStaticImport(staticAttributeValue);
          }
        })
        .forEach(outputDocument::remove);
  }

  private static boolean isScopedStyleElement(Element element) {
    return element != null && "style".equalsIgnoreCase(element.getName())
        && element.getAttributes() != null && element.getAttributes().get("scoped") != null;
  }

  private String processScopedCss(Source doc) {
    final String[] scopedCss = new String[1];

    doc.getAllElements().stream()
        .filter(TemplateParser::isScopedStyleElement)
        .peek(styleScoped -> {
          String css = styleScoped.getContent().toString().trim();
          if (!css.isEmpty()) {
            String lang = styleScoped.getAttributeValue("lang");
            if ("scss".equalsIgnoreCase(lang)) { // lang="scss"
              css = scssToCss(css);
            }
            TemplateScopedCssParser scopedCssParser = new TemplateScopedCssParser(messager);
            Optional<ScopedCssResult> scopedCssResult = scopedCssParser.parse(
                context.getComponentTypeElement(), css);
            if (scopedCssResult.isPresent()) {
              context.getMandatoryAttributes().putAll(scopedCssResult.get().mandatoryAttributes);
              scopedCss[0] = scopedCssResult.get().scopedCss;
            }
          }
        })
        .forEach(outputDocument::remove);

    return scopedCss[0];
  }

  private String scssToCss(String scss) {
    Compiler compiler = new Compiler();
    Options options = new Options();
    try {
      StringContext context = new StringContext(scss, this.htmlTemplateUri, null/*outputPath*/,
          options);
      Output output = compiler.compile(context);
      return output.getCss();
    } catch (CompilationException e) {
      logger.error("SCSS compile failed: " + e.getErrorText());
      throw new RuntimeException(e);
    }
  }

  /**
   * Recursive method that will process the whole template DOM tree.
   *
   * @param element Current element being processed
   */
  private void processElement(Element element) {
    context.setCurrentSegment(element);
    currentProp = null;
    currentAttribute = null;

    // Process attributes
    boolean shouldPopContext = processElementAttributes(element);

    // Process text segments
    StreamSupport
        .stream(((Iterable<Segment>) element::getNodeIterator).spliterator(), false)
        .filter(segment -> !(segment instanceof Tag)
            && !(segment instanceof CharacterReference))
        .filter(segment -> {
          for (Element child : element.getChildElements()) {
            if (child.encloses(segment)) {
              return false;
            }
          }
          return true;
        })
        .forEach(this::processTextNode);

    // Recurse downwards
    element.getChildElements().
        forEach(this::processElement);

    // After downward recursion, pop the context layer if necessary
    if (shouldPopContext) {
      context.popContextLayer();
    }
  }

  /**
   * Process a given {@link Element} attributes
   *
   * @param element The element to process
   * @return True if this {@link Element} created a new Context in the stack, false otherwise
   */
  private boolean processElementAttributes(Element element) {
    Attributes attributes = element.getAttributes();
    if (attributes == null) {
      return false;
    }

    boolean shouldPopContext = false;
    registerMandatoryAttributes(attributes);

    Attribute vForAttribute = attributes.get("v-for");
    Attribute slotScopeAttribute = attributes.get("slot-scope");
    Attribute vSlotAttribute = null;
    for (Attribute attribute : attributes) {
      if (isVSlot(attribute)) {
        vSlotAttribute = attribute;
      }
    }

    if (vForAttribute != null || slotScopeAttribute != null || (vSlotAttribute != null
        && !vSlotAttribute.getValue()
        .isEmpty())) {
      // Add a context layer
      shouldPopContext = true;
      context.addContextLayer(vForAttribute != null);
    }

    if (vForAttribute != null) {
      String processedVForValue = processVForValue(vForAttribute.getValue());
      outputDocument.replace(vForAttribute.getValueSegment(), processedVForValue);
    }

    if (slotScopeAttribute != null) {
      String processedScopedSlotValue = processSlotScopeValue(slotScopeAttribute.getValue());
      outputDocument.replace(slotScopeAttribute.getValueSegment(), processedScopedSlotValue);
    }

    if (vSlotAttribute != null) {
      String processedScopedSlotValue = processSlotScopeValue(vSlotAttribute.getValue());
      outputDocument.replace(vSlotAttribute.getValueSegment(), processedScopedSlotValue);
    }

    Optional<LocalComponent> localComponent = getLocalComponentForElement(element);

    Attribute refAttribute = attributes.get("ref");
    if (refAttribute != null) {
      result.addRef(
          refAttribute.getValue(),
          localComponent.map(LocalComponent::getComponentType)
              .orElse(getTypeFromDOMElement(element)),
          context.isInVFor()
      );
    }

    Map<String, Class<?>> elementPropertiesType = getPropertiesForDOMElement(element)
        .orElse(new HashMap<>());

    // Iterate on element attributes
    Set<LocalComponentProp> foundProps = new HashSet<>();
    for (Attribute attribute : element.getAttributes()) {
      String key = attribute.getKey();
      if ("v-for".equals(key) || "slot-scope".equals(key) || isVSlot(attribute)) {
        continue;
      }

      if ("v-model".equals(key)) {
        processVModel(attribute);
        continue;
      }

      if (isAttributeBinding(attribute) && key.toLowerCase().endsWith(".sync")) {
        processSyncProp(attribute);
        continue;
      }

      Optional<LocalComponentProp> optionalProp =
          localComponent.flatMap(lc -> lc.getPropForAttribute(attribute.getName()));
      optionalProp.ifPresent(foundProps::add);

      if (!VUE_ATTR_PATTERN.matcher(key).matches()) {
        if (optionalProp.isPresent()) {
          LocalComponentProp prop = optionalProp.get();
          if (!this.isBooleanBinding(attribute, prop)) {
            this.validateStringPropBinding(prop);
          }
        }
        continue;
      }

      context.setCurrentSegment(attribute);

      currentAttribute = attribute;
      currentProp = optionalProp.orElse(null);
      currentExpressionReturnType = getExpressionReturnTypeForAttribute(attribute,
          elementPropertiesType);
      String processedExpression = processExpression(attribute.getValue());

      if (attribute.hasValue()) {
        outputDocument.replace(attribute.getValueSegment(), processedExpression);
      }
    }

    localComponent.ifPresent(lc -> validateRequiredProps(lc, foundProps));

    return shouldPopContext;
  }

  /**
   * Find the corresponding TypeMirror from Elemental2 for a given DOM Element
   *
   * @param element The element we want the TypeMirror of
   * @return The type mirror
   */
  private TypeMirror getTypeFromDOMElement(Element element) {
    return DOMElementsUtil
        .getTypeForElementTag(element.getStartTag().getName())
        .map(Class::getCanonicalName)
        .map(elements::getTypeElement)
        .map(TypeElement::asType)
        .orElse(null);
  }

  private Optional<Map<String, Class<?>>> getPropertiesForDOMElement(Element element) {
    return DOMElementsUtil
        .getTypeForElementTag(element.getStartTag().getName())
        .map(DOMElementsUtil::getElementProperties);
  }

  /**
   * Register mandatory attributes for Scoped CSS
   */
  private void registerMandatoryAttributes(Attributes attributes) {
    // see https://sourceforge.net/p/jerichohtml/discussion/350024/thread/501a7d05/
    Map<String, String> attrs = context.getMandatoryAttributes();
    if (!attrs.isEmpty()) {
      for (Entry<String, String> i : attrs.entrySet()) {
        String v = i.getValue();
        if (v == null) {
          outputDocument.insert(attributes.getBegin(), " " + i.getKey() + " ");
        } else {
          outputDocument.insert(attributes.getBegin(), " " + i.getKey() + "\"" + v + "\" ");
        }
      }
    }
  }

  /**
   * Process text node to check for {{ }} vue expressions.
   *
   * @param textSegment Current segment being processed
   */
  private void processTextNode(Segment textSegment) {
    context.setCurrentSegment(textSegment);
    String elementText = textSegment.toString();

    Matcher matcher = VUE_MUSTACHE_PATTERN.matcher(elementText);

    int lastEnd = 0;
    StringBuilder newText = new StringBuilder();
    while (matcher.find()) {
      int start = matcher.start();
      int end = matcher.end();
      if (start > 0) {
        newText.append(elementText, lastEnd, start);
      }

      currentExpressionReturnType = TypeName.get(String.class);
      String expressionString = elementText.substring(start + 2, end - 2).trim();
      String processedExpression = processExpression(expressionString);
      newText.append("{{ ").append(processedExpression).append(" }}");
      lastEnd = end;
    }
    if (lastEnd > 0) {
      newText.append(elementText.substring(lastEnd));
      outputDocument.replace(textSegment, newText.toString());
    }
  }

  /**
   * Process v-model expressions. Only @Data fields are allowed in v-model.
   *
   * @param vModelAttribute The v-model attribute to process
   */
  private void processVModel(Attribute vModelAttribute) {
    String vModelValue = vModelAttribute.getValue();
    VariableInfo vModelDataField = context.findRootVariable(vModelValue);
    if (vModelDataField == null) {
      if (vModelValue.contains(".")) {
        logger.error("v-model doesn't support dot notation in Vue GWT: \"" + vModelValue
            + "\". Try using a @Computed with a getter and a setter. Check our documentation on v-model for more information.");
      } else {
        logger.error("Couldn't find @Data or @Computed for v-model \"" + vModelValue
            + "\". V-Model is only supported on @Data and @Computed. Check our documentation on v-model for more information.");
      }
      return;
    }

    String placeHolderVModelValue = markedDataFieldToPlaceHolderField(vModelValue);
    outputDocument.replace(vModelAttribute.getValueSegment(), placeHolderVModelValue);
    result.addMarkedDataField(vModelDataField);
  }

  /**
   * Process :prop.sync expressions. Only @Data fields are allowed on .sync.
   *
   * @param syncPropAttribute The sync attribute to process
   */
  private void processSyncProp(Attribute syncPropAttribute) {
    String syncFieldValue = syncPropAttribute.getValue();
    VariableInfo vModelDataField = context.findRootVariable(syncFieldValue);
    if (vModelDataField == null) {
      if (syncFieldValue.contains(".")) {
        logger.error(".sync doesn't support dot notation in Vue GWT: \"" + syncFieldValue
            + "\". Try using a @Computed with a getter and a setter. Check our documentation on .sync for more information.");
      } else {
        logger.error("Couldn't find @Data or @Computed for .sync \"" + syncFieldValue
            + "\". V-Model is only supported on @Data and @Computed. Check our documentation on .sync for more information.");
      }
      return;
    }

    String placeHolderSyncValue = markedDataFieldToPlaceHolderField(syncFieldValue);
    outputDocument.replace(syncPropAttribute.getValueSegment(), placeHolderSyncValue);
    result.addMarkedDataField(vModelDataField);
  }

  /**
   * Return the {@link LocalComponent} definition for a given DOM {@link Element}
   *
   * @param element Current element being processed
   * @return An Optional {@link LocalComponent}
   */
  private Optional<LocalComponent> getLocalComponentForElement(Element element) {
    String componentName = element.getAttributes().getValue("is");
    if (componentName == null) {
      componentName = element.getStartTag().getName();
    }

    return context.getLocalComponent(componentName);
  }

  /**
   * Check whether this is a boolean binding in the form: <component is-something />
   *
   * @param attribute The attribute we are using to bind
   * @param prop The prop we are binding to
   * @return true if this is a boolean binding, false otherwise
   */
  private boolean isBooleanBinding(Attribute attribute, LocalComponentProp prop) {
    TypeName type = prop.getType();
    if (type.isBoxedPrimitive()) {
      type = type.unbox();
    }

    return type.equals(TypeName.BOOLEAN) && !attribute.hasValue();
  }

  /**
   * Validate that a {@link Prop} bounded with string binding is of type String
   *
   * @param localComponentProp The prop to check
   */
  private void validateStringPropBinding(LocalComponentProp localComponentProp) {
    if (localComponentProp.getType().toString().equals(String.class.getCanonicalName())) {
      return;
    }

    logger.error("Passing a String to a non String Prop: \""
        + localComponentProp.getPropName()
        + "\". "
        + "If you want to pass a boolean or an int you should use v-bind. "
        + "For example: v-bind:my-prop=\"12\" (or using the short syntax, :my-prop=\"12\") instead of my-prop=\"12\".");
  }

  /**
   * Validate that all the required properties of a local components are present on a the HTML
   * {@link Element} that represents it.
   *
   * @param localComponent The {@link LocalComponent} we want to check
   * @param foundProps The props we found on the HTML {@link Element} during processing
   */
  private void validateRequiredProps(LocalComponent localComponent,
      Set<LocalComponentProp> foundProps) {
    String missingRequiredProps = localComponent
        .getRequiredProps()
        .stream()
        .filter(prop -> !foundProps.contains(prop))
        .map(prop -> "\"" + propNameToAttributeName(prop.getPropName()) + "\"")
        .collect(Collectors.joining(","));

    if (!missingRequiredProps.isEmpty()) {
      logger.error("Missing required property: "
          + missingRequiredProps
          + " on child component \""
          + localComponent.getComponentTagName()
          + "\"");
    }
  }

  /**
   * Guess the type of the expression based on where it is used. The guessed type can be overridden
   * by adding a Cast to the desired type at the beginning of the expression.
   *
   * @param attribute The attribute the expression is in
   */
  private TypeName getExpressionReturnTypeForAttribute(Attribute attribute,
      Map<String, Class<?>> propertiesTypes) {
    String attributeName = attribute.getKey().toLowerCase();

    if (attributeName.indexOf("@") == 0 || attributeName.indexOf("v-on:") == 0) {
      return TypeName.VOID;
    }

    if ("v-if".equals(attributeName) || "v-show".equals(attributeName)) {
      return TypeName.BOOLEAN;
    }

    if (isBoundedAttribute(attributeName)) {
      String unboundedAttributeName = boundedAttributeToAttributeName(attributeName);

      if (unboundedAttributeName.equals("class") || unboundedAttributeName.equals("style")) {
        return TypeName.get(Any.class);
      }

      if (propertiesTypes.containsKey(unboundedAttributeName)) {
        return TypeName.get(propertiesTypes.get(unboundedAttributeName));
      }
    }

    if (currentProp != null) {
      return currentProp.getType();
    }

    return TypeName.get(Any.class);
  }

  /**
   * Process a v-for value. It will register the loop variables as a local variable in the context
   * stack.
   *
   * @param vForValue The value of the v-for attribute
   * @return A processed v-for value, should be placed in the HTML in place of the original v-for
   * value
   */
  private String processVForValue(String vForValue) {
    VForDefinition vForDef = new VForDefinition(vForValue, context, logger);

    // Set return of the "in" expression
    currentExpressionReturnType = vForDef.getInExpressionType();

    String inExpression = this.processExpression(vForDef.getInExpression());

    // And return the newly built definition
    return vForDef.getVariableDefinition() + " in " + inExpression;
  }

  /**
   * Process a scoped-slot value.
   */
  private String processSlotScopeValue(String value) {
    SlotScopeDefinition slotScopeDefinition = new SlotScopeDefinition(value, context, logger);
    return slotScopeDefinition.getSlotScopeVariableName();
  }

  /**
   * Process a given template expression
   *
   * @param expressionString Should be either empty or a valid Java expression
   * @return The processed expression
   */
  private String processExpression(String expressionString) {
    expressionString = expressionString == null ? "" : expressionString.trim();
    if (expressionString.isEmpty()) {
      if (isAttributeBinding(currentAttribute)) {
        logger.error(
            "Empty expression in template property binding. If you want to pass an empty string then simply don't use binding: my-attribute=\"\"",
            currentAttribute.toString());
      } else if (isEventBinding(currentAttribute)) {
        logger.error("Empty expression in template event binding.",
            currentAttribute.toString());
      } else {
        return "";
      }
    }

    if (expressionString.startsWith("{")) {
      logger.error(
          "Object literal syntax are not supported yet in Vue GWT, please use map(e(\"key1\", myValue), e(\"key2\", myValue2 > 5)...) instead. The object returned by map() is a regular Javascript Object (JsObject) with the given key/values.",
          expressionString);
    }

    if (expressionString.startsWith("[")) {
      logger.error(
          "Array literal syntax are not supported yet in Vue GWT, please use array(myValue, myValue2 > 5...) instead. The object returned by array() is a regular Javascript Array (JsArray) with the given values.",
          expressionString);
    }

    if (shouldSkipExpressionProcessing(expressionString)) {
      return expressionString;
    }

    return processJavaExpression(expressionString).toTemplateString();
  }

  /**
   * In some cases we want to skip expression processing for optimization. This is when we are sure
   * the expression is valid and there is no need to create a Java method for it.
   *
   * @param expressionString The expression to asses
   * @return true if we can skip the expression
   */
  private boolean shouldSkipExpressionProcessing(String expressionString) {
    // We don't skip if it's a component prop as we want Java validation
    // We don't optimize String expression, as we want GWT to convert Java values
    // to String for us (Enums, wrapped primitives...)
    return currentProp == null && !String.class
        .getCanonicalName()
        .equals(currentExpressionReturnType.toString()) && isSimpleVueJsExpression(
        expressionString);
  }

  private static boolean isAttributeBinding(Attribute attribute) {
    String attributeName = attribute.getKey().toLowerCase();
    return attributeName.startsWith(":") || attributeName.startsWith("v-bind:");
  }

  private static boolean isEventBinding(Attribute attribute) {
    String attributeName = attribute.getKey().toLowerCase();
    return attributeName.startsWith("@") || attributeName.startsWith("v-on:");
  }

  private static boolean isVSlot(Attribute attribute) {
    String attributeName = attribute.getKey().toLowerCase();
    return attributeName.startsWith("v-slot");
  }

  /**
   * In some case the expression is already a valid Vue.js expression that will work without any
   * processing. In this case we just leave it in place. This avoid creating Computed
   * properties/methods for simple expressions.
   *
   * @param expressionString The expression to check
   * @return true if it's already a valid Vue.js expression, false otherwise
   */
  private boolean isSimpleVueJsExpression(String expressionString) {
    String methodName = expressionString;
    if (expressionString.endsWith("()")) {
      methodName = expressionString.substring(0, expressionString.length() - 2);
    }

    return context.hasMethod(methodName);
  }

  /**
   * Process the given string as a Java expression.
   *
   * @param expressionString A valid Java expression
   * @return A processed expression, should be placed in the HTML in place of the original
   * expression
   */
  private TemplateExpression processJavaExpression(String expressionString) {
    Optional<Expression> maybeExpression = this.javaParser.parseExpression(expressionString)
        .getResult();

    if (!maybeExpression.isPresent()) {
      logger.error("Couldn't parse Expression, make sure it is valid Java.",
          expressionString);
      throw new RuntimeException();
    }

    Expression expression = maybeExpression.get();
    resolveTypesUsingImports(expression);
    resolveStaticMethodsUsingImports(expression);

    checkMethodNames(expression);

    // Find the parameters used by the expression
    List<VariableInfo> expressionParameters = new LinkedList<>();
    findExpressionParameters(expression, expressionParameters);

    // If there is a cast first, we use this as the type of our expression
    if (currentProp == null) {
      expression = getTypeFromCast(expression);
    }

    // Update the expression as it might have been changed
    expressionString = expression.toString();

    // Add the resulting expression to our result
    return result.addExpression(expressionString,
        currentExpressionReturnType,
        currentProp == null,
        expressionParameters);
  }

  /**
   * Get the type of an expression from the cast at the beginning. (int) 12 -> 12 of type int (int)
   * 15 + 5 -> 15 + 5 of type int (float) (12 + 3) -> 12 + 3 of type float ((int) 12) + 3 -> ((int)
   * 12) + 3 of type Any ((JsArray) myArray).getAt(0) -> ((JsArray) myArray).getAt(0) of type Any
   *
   * @param expression The expression to process
   * @return The modified expression (where cast has been removed if necessary)
   */
  private Expression getTypeFromCast(Expression expression) {
    if (expression instanceof BinaryExpr) {
      Expression mostLeft = getLeftmostExpression(expression);
      if (mostLeft instanceof CastExpr) {
        CastExpr castExpr = (CastExpr) mostLeft;
        currentExpressionReturnType = stringTypeToTypeName(castExpr.getType().toString());
        BinaryExpr parent = (BinaryExpr) mostLeft.getParentNode().get();
        parent.setLeft(castExpr.getExpression());
      }
    } else if (expression instanceof CastExpr) {
      CastExpr castExpr = (CastExpr) expression;
      currentExpressionReturnType = stringTypeToTypeName(castExpr.getType().toString());
      expression = castExpr.getExpression();
    }
    return expression;
  }

  private Expression getLeftmostExpression(Expression expression) {
    if (expression instanceof BinaryExpr) {
      return getLeftmostExpression(((BinaryExpr) expression).getLeft());
    }

    return expression;
  }

  /**
   * Resolve all the types in the expression. This will replace the Class with the full qualified
   * name using the template imports.
   *
   * @param expression A Java expression from the Template
   */
  private void resolveTypesUsingImports(Expression expression) {
    if (expression instanceof NodeWithType) {
      NodeWithType<?, ?> nodeWithType = ((NodeWithType<?, ?>) expression);
      nodeWithType.setType(getQualifiedName(nodeWithType.getType()));
    }

    // Recurse downward in the expression
    expression
        .getChildNodes()
        .stream()
        .filter(Expression.class::isInstance)
        .map(Expression.class::cast)
        .forEach(this::resolveTypesUsingImports);
  }

  /**
   * Resolve static method calls using static imports
   *
   * @param expression The expression to resolve
   */
  private void resolveStaticMethodsUsingImports(Expression expression) {
    if (expression instanceof MethodCallExpr) {
      MethodCallExpr methodCall = ((MethodCallExpr) expression);
      String methodName = methodCall.getName().getIdentifier();
      if (!methodCall.getScope().isPresent() && context.hasStaticMethod(methodName)) {
        methodCall.setName(context.getFullyQualifiedNameForMethodName(methodName));
      }
    }

    // Recurse downward in the expression
    expression
        .getChildNodes()
        .stream()
        .filter(Expression.class::isInstance)
        .map(Expression.class::cast)
        .forEach(this::resolveStaticMethodsUsingImports);
  }

  /**
   * Check the expression for component method calls. This will check that the methods used in the
   * template exist in the Component. It throws an exception if we use a method that is not declared
   * in our Component. This will not check for the type or number of parameters, we leave that to
   * the Java Compiler.
   *
   * @param expression The expression to check
   */
  private void checkMethodNames(Expression expression) {
    if (expression instanceof MethodCallExpr) {
      MethodCallExpr methodCall = ((MethodCallExpr) expression);
      if (!methodCall.getScope().isPresent()) {
        String methodName = methodCall.getName().getIdentifier();
        if (!context.hasMethod(methodName) && !context.hasStaticMethod(methodName)) {
          logger.error("Couldn't find the method \""
              + methodName
              + "\". "
              + "Make sure it is not private.");
        }
      }
    }

    for (com.github.javaparser.ast.Node node : expression.getChildNodes()) {
      if (!(node instanceof Expression)) {
        continue;
      }

      Expression childExpr = (Expression) node;
      checkMethodNames(childExpr);
    }
  }

  /**
   * Find all the parameters this expression depends on. This is either the local variables (from a
   * v-for loop) or the $event variable.
   *
   * @param expression An expression from the Template
   * @param parameters The parameters this expression depends on
   */
  private void findExpressionParameters(Expression expression, List<VariableInfo> parameters) {
    if (expression instanceof NameExpr) {
      NameExpr nameExpr = ((NameExpr) expression);
      if ("$event".equals(nameExpr.getNameAsString())) {
        processEventParameter(expression, nameExpr, parameters);
      } else {
        processNameExpression(nameExpr, parameters);
      }
    }

    expression
        .getChildNodes()
        .stream()
        .filter(Expression.class::isInstance)
        .map(Expression.class::cast)
        .forEach(exp -> findExpressionParameters(exp, parameters));
  }

  /**
   * Process the $event variable passed on v-on. This variable must have a valid cast in front.
   *
   * @param expression The currently processed expression
   * @param nameExpr The variable we are processing
   * @param parameters The parameters this expression depends on
   */
  private void processEventParameter(Expression expression, NameExpr nameExpr,
      List<VariableInfo> parameters) {
    Optional<Node> parentNode = nameExpr.getParentNode();
    if (parentNode.isPresent() && parentNode.get() instanceof CastExpr) {
      CastExpr castExpr = (CastExpr) parentNode.get();
      parameters.add(new VariableInfo(castExpr.getType().toString(), "$event"));
    } else {
      logger.error(
          "\"$event\" should always be casted to it's intended type. Example: @click=\"doSomething((Event) $event)\".",
          expression.toString());
    }
  }

  /**
   * Process a name expression to determine if it exists in the context. If it does, and it's a
   * local variable (from a v-for) we add it to our parameters
   *
   * @param nameExpr The variable we are processing
   * @param parameters The parameters this expression depends on
   */
  private void processNameExpression(NameExpr nameExpr,
      List<VariableInfo> parameters) {
    String name = nameExpr.getNameAsString();
    if (context.hasImport(name)) {
      // This is a direct Class reference, we just replace with the fully qualified name
      nameExpr.setName(context.getFullyQualifiedNameForClassName(name));
      return;
    }

    if (context.hasStaticProperty(name)) {
      nameExpr.setName(context.getFullyQualifiedNameForPropertyName(name));
      return;
    }

    VariableInfo variableInfo = context.findVariable(name);
    if (variableInfo == null) {
      logger.error("Couldn't find variable/method \""
          + name
          + "\". Make sure you didn't forget the @Data/@Prop annotation.");
    }

    if (variableInfo instanceof LocalVariableInfo) {
      parameters.add(variableInfo);
    } else if (variableInfo instanceof DestructuredPropertyInfo) {
      DestructuredPropertyInfo propertyInfo = (DestructuredPropertyInfo) variableInfo;

      parameters.add(propertyInfo.getDestructuredVariable());
      nameExpr.setName(propertyInfo.getAsDestructuredValue());
    } else if (variableInfo instanceof ComputedVariableInfo) {
      nameExpr.setName(((ComputedVariableInfo) variableInfo).getFieldName());
    }
  }

  private String getQualifiedName(Type type) {
    return context.getFullyQualifiedNameForClassName(type.toString());
  }
}
