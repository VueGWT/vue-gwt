package com.axellience.vuegwt.core.client.tools;

import static jsinterop.base.Js.asAny;
import static jsinterop.base.Js.cast;
import static jsinterop.base.Js.isTripleEqual;
import static jsinterop.base.Js.uncheckedCast;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.vue.VueJsConstructor;
import elemental2.core.Function;
import elemental2.core.JsArray;
import elemental2.core.JsObject;
import elemental2.core.JsRegExp;
import elemental2.core.JsString;
import java.util.HashSet;
import java.util.Set;
import jsinterop.annotations.JsFunction;
import jsinterop.base.Any;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

/**
 * This object provides utils methods for VueGWT internal processing
 *
 * @author Adrien Baron
 */
public class VueGWTTools {

  private static final JsRegExp ESCAPE_JS_STRING_REGEXP = new JsRegExp("[.*+?^${}()|[\\]\\\\]",
      "g");

  public static final String MARKING_STRING = "VUE_GWT";

  /**
   * Copy a Java class prototype to a VueComponent declaration. This allows VueComponent created by
   * Vue to pass as an instance of the {@link IsVueComponent} class they represent.
   *
   * @param extendedVueJsConstructor The Vue.js constructor function to extend
   * @param javaComponentPrototype The {@link IsVueComponent} class JS prototype to extend with
   * @param <T> The type of the VueComponent
   */
  public static <T extends IsVueComponent> void extendVueConstructorWithJavaPrototype(
      VueJsConstructor<T> extendedVueJsConstructor,
      JsPropertyMap<Object> javaComponentPrototype) {
    JsPropertyMap vueProto =
        (JsPropertyMap) ((JsPropertyMap) extendedVueJsConstructor).get("prototype");
    JsObject vueProtoJsObject = ((JsObject) vueProto);
    javaComponentPrototype.forEach(protoProp -> {
      if (!vueProtoJsObject.hasOwnProperty(protoProp)) {
        vueProto.set(protoProp, javaComponentPrototype.get(protoProp));
      }
    });
  }

  /**
   * Proxy a method call to be warned when it called. This requires the function to be JsInterop
   * (name shouldn't change at runtime). This used to observe Java Collections/Map. It won't be
   * necessary in future versions of Vue.js based on ES6 proxies.
   *
   * @param object The object to observe
   * @param methodName The name of the method to proxify
   * @param afterMethodCall A callback called each time after the method has been executed
   * @param <T> Type of the object the we Proxy
   */
  public static <T> void wrapMethod(T object, String methodName,
      AfterMethodCall<T> afterMethodCall) {
    Function method = (Function) ((JsPropertyMap) object).get(methodName);

    WrappingFunction wrappingFunction = args -> {
      Object result = method.apply(object, args);
      afterMethodCall.execute(object, methodName, result, args);
      return result;
    };

    ((JsPropertyMap) object).set(methodName, wrappingFunction);
  }

  @FunctionalInterface
  @JsFunction
  private interface WrappingFunction {

    Object call(Object... args);
  }

  /**
   * Return a "deep" value in a given object by following an expression in the form:
   * "parent.child.property". This only works if all the chain is exposed using JsInterop.
   *
   * @param object The root object to get on
   * @param path The path to follow
   * @param <T> The type of object we get in return
   * @return The object at the end of the chain
   */
  public static <T> T getDeepValue(Object object, String path) {
    JsPropertyMap objectMap = (JsPropertyMap) object;
    String[] pathSplit = path.split("\\.");
    for (String s : pathSplit) {
      objectMap = (JsPropertyMap) objectMap.get(s);
    }
    return (T) objectMap;
  }

  /**
   * Convert a template expression to String while keeping null values
   *
   * @param expressionValue The value of the expression from the template
   * @return Null if passed null, the value to String otherwise
   */
  public static String templateExpressionToString(Object expressionValue) {
    return expressionValue == null ? null : expressionValue + "";
  }

  /**
   * Convert a char[] to a String.
   *
   * @param expressionValue The value of the expression from the template
   * @return Null if passed null, the value to String otherwise
   */
  public static String templateExpressionToString(char[] expressionValue) {
    return expressionValue == null ? null : String.valueOf(expressionValue);
  }

  public static boolean isObservableValue(JsPropertyMap<Object> value) {
    return value != null &&
        (JsArray.isArray(value) || "object".equals(Js.typeof(value))) &&
        !value.has("_isVue");
  }

  /**
   * Determine the name of fields at runtime. This allows fields to be renamed during optimizations.
   * The fieldsMarker method must set the value of the wanted fields to either null, 0 or false
   * (depending on the type of the field).
   *
   * @param instance The instance we want to get the name of the fields from
   * @param fieldsMarker Mark the fields which names we want to get
   * @return The list of names of the fields
   */
  public static Set<String> getFieldsName(Object instance, Runnable fieldsMarker) {
    JsPropertyMap<Object> map = cast(instance);
    JsObject jsObject = cast(instance);
    map.forEach(key -> {
      if (!jsObject.hasOwnProperty(key)) {
        return;
      }

      Any val = asAny(map.get(key));
      if (isTripleEqual(val, null) ||
          isTripleEqual(val, asAny(1)) ||
          isTripleEqual(val, asAny(true)) ||
          isTripleEqual(val, MARKING_STRING)) {
        map.delete(key);
      }
    });

    fieldsMarker.run();

    Set<String> dataFields = new HashSet<>();
    map.forEach(key -> {
      if (!jsObject.hasOwnProperty(key)) {
        return;
      }

      Any val = asAny(map.get(key));
      if (isTripleEqual(val, null) ||
          isTripleEqual(val, asAny(1)) ||
          isTripleEqual(val, asAny(true)) ||
          isTripleEqual(val, MARKING_STRING)) {
        dataFields.add(key);
      }
    });

    return dataFields;
  }

  public static String getFieldName(Object instance, Runnable fieldMarker) {
    return getFieldsName(instance, fieldMarker).iterator().next();
  }

  /**
   * Init instance properties for the given VueComponent instance. The Constructor for VueComponent
   * is provided by Vue and doesn't extend the {@link IsVueComponent} constructor. This method get
   * an instance of the Java class for the VueComponent and copy properties to the
   * VueComponentInstance. This will initialise properties that are initialised inline in the class.
   * For example: List&lt;String&gt; myList = new LinkedList&lt;String&gt;();
   *
   * @param vueComponentInstance An instance of VueComponent to initialize
   * @param javaComponentClassInstance An instance of the Component class
   */
  public static void initComponentInstanceFields(IsVueComponent vueComponentInstance,
      IsVueComponent javaComponentClassInstance) {
    JsPropertyMap<Object> vueComponentInstancePropertyMap = Js.cast(vueComponentInstance);
    JsPropertyMap<Object> javaComponentClassInstancePropertyMap = Js
        .cast(javaComponentClassInstance);

    javaComponentClassInstancePropertyMap.forEach(key -> {
      if (!javaComponentClassInstancePropertyMap.has(key)
          || vueComponentInstancePropertyMap.get(key) != null) {
        return;
      }
      vueComponentInstancePropertyMap.set(key, javaComponentClassInstancePropertyMap.get(key));
    });
  }

  /**
   * Escape a String to be used in a JsRegexp as a String literal
   *
   * @param input The String we want to escape
   * @return The escaped String
   */
  public static String escapeStringForJsRegexp(String input) {
    JsString string = uncheckedCast(input);
    return string.replace(ESCAPE_JS_STRING_REGEXP, "\\$&");
  }

  public static String replaceVariableInRenderFunction(String renderFunctionString,
      String placeHolderFieldName, IsVueComponent component, Runnable fieldMarker) {
    JsString renderFunctionJsString = uncheckedCast(renderFunctionString);

    return renderFunctionJsString.replace(
        new JsRegExp(escapeStringForJsRegexp(placeHolderFieldName), "g"),
        VueGWTTools.getFieldName(component, fieldMarker)
    );
  }
}
