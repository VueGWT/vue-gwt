package com.axellience.vuegwt.core.annotations.component;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Mark a Java property as being a Vue component Property passed from your Component parent
 *
 * @author Adrien Baron
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Prop {

  /**
   * Is the property required
   *
   * @return true if the property is required, false otherwise. Default to false.
   */
  boolean required() default false;

  /**
   * Should check the type of the property. By default check based on the Java type.
   *
   * @return true if we should check the type, false otherwise. Default to true.
   */
  boolean checkType() default true;
}
