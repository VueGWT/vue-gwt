package com.axellience.vuegwt.processors.utils;

import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwt.core.client.tools.VueGWTTools;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec.Builder;
import java.beans.Introspector;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;

/**
 * General generator util methods
 *
 * @author Adrien Baron
 */
public class GeneratorsUtil {

  public static boolean hasInterface(ProcessingEnvironment processingEnv, TypeMirror type,
      Class myInterface) {
    TypeMirror interfaceType =
        processingEnv.getElementUtils().getTypeElement(myInterface.getCanonicalName()).asType();

    return processingEnv.getTypeUtils().isAssignable(type, interfaceType);
  }

  public static String getComputedPropertyName(ExecutableElement method) {
    Computed computed = method.getAnnotation(Computed.class);
    String methodName = method.getSimpleName().toString();

    if (!"".equals(computed.value())) {
      return computed.value();
    }

    if (methodName.startsWith("get") || methodName.startsWith("set")) {
      return Introspector.decapitalize(methodName.substring(3));
    }

    if (methodName.startsWith("is")) {
      return Introspector.decapitalize(methodName.substring(2));
    }

    return methodName;
  }

  public static void toJavaFile(Filer filer, Builder classBuilder, ClassName className,
      TypeElement... originatingElement) {
    try {
      JavaFile javaFile =
          JavaFile.builder(className.packageName(), classBuilder.build()).build();

      JavaFileObject javaFileObject =
          filer.createSourceFile(className.reflectionName(), originatingElement);

      Writer writer = javaFileObject.openWriter();
      javaFile.writeTo(writer);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static boolean hasAnnotation(Element element,
      Class<? extends Annotation> annotationClass) {
    return element.getAnnotation(annotationClass) != null;
  }

  public static AnnotationSpec getUnusableByJSAnnotation() {
    return AnnotationSpec
        .builder(SuppressWarnings.class)
        .addMember("value", "$S", "unusable-by-js")
        .build();
  }

  public static TypeName stringTypeToTypeName(String type) {
    if (type.equals("void") || "VOID".equals(type)) {
      return TypeName.VOID;
    }
    if (type.equals("boolean")) {
      return TypeName.BOOLEAN;
    }
    if (type.equals("byte")) {
      return TypeName.BYTE;
    }
    if (type.equals("short")) {
      return TypeName.SHORT;
    }
    if (type.equals("int")) {
      return TypeName.INT;
    }
    if (type.equals("long")) {
      return TypeName.LONG;
    }
    if (type.equals("char")) {
      return TypeName.CHAR;
    }
    if (type.equals("float")) {
      return TypeName.FLOAT;
    }
    if (type.equals("double")) {
      return TypeName.DOUBLE;
    }
    if (type.equals("Object") || type.equals("java.lang.Object")) {
      return TypeName.OBJECT;
    }

    if (type.equals("Void") || type.equals("java.lang.Void")) {
      return TypeName.VOID.box();
    }
    if (type.equals("Boolean") || type.equals("java.lang.Boolean")) {
      return TypeName.BOOLEAN.box();
    }
    if (type.equals("Byte") || type.equals("java.lang.Byte")) {
      return TypeName.BYTE.box();
    }
    if (type.equals("Short") || type.equals("java.lang.Short")) {
      return TypeName.SHORT.box();
    }
    if (type.equals("Int") || type.equals("java.lang.Int")) {
      return TypeName.INT.box();
    }
    if (type.equals("Long") || type.equals("java.lang.Long")) {
      return TypeName.LONG.box();
    }
    if (type.equals("Char") || type.equals("java.lang.Char")) {
      return TypeName.CHAR.box();
    }
    if (type.equals("Float") || type.equals("java.lang.Float")) {
      return TypeName.FLOAT.box();
    }
    if (type.equals("Double") || type.equals("java.lang.Double")) {
      return TypeName.DOUBLE.box();
    }

    // Manage array types
    Pattern arrayEnding = Pattern.compile("\\[\\]");
    Matcher matcher = arrayEnding.matcher(type);
    int arrayCount = 0;
    while (matcher.find()) {
      arrayCount++;
    }

    if (arrayCount > 0) {
      type = type.substring(0, type.length() - arrayCount * 2);
    }

    TypeName typeName = ClassName.bestGuess(type);
    for (int i = 0; i < arrayCount; i++) {
      typeName = ArrayTypeName.of(typeName);
    }

    return typeName;
  }


  /**
   * Return the value used to mark a field depending on it's type
   *
   * @param typeMirror The type of the field
   * @return A String representing the value
   * @see VueGWTTools#getFieldsName(Object, Runnable)
   */
  public static String getFieldMarkingValueForType(TypeMirror typeMirror) {
    return getFieldMarkingValueForType(TypeName.get(typeMirror));
  }

  /**
   * Return the value used to mark a field depending on it's type
   *
   * @param fieldType The type of the field
   * @return A String representing the value
   * @see VueGWTTools#getFieldsName(Object, Runnable)
   */
  public static String getFieldMarkingValueForType(TypeName fieldType) {
    if (fieldType == TypeName.BOOLEAN) {
      return "false";
    } else if (fieldType.isPrimitive()) {
      return "0";
    }

    return "null";
  }
}
