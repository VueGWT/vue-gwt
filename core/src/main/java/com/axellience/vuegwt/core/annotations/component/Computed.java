package com.axellience.vuegwt.core.annotations.component;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Mark a computed property
 *
 * @author Adrien Baron
 */
@Target(METHOD)
@Retention(CLASS)
public @interface Computed {

  String value() default "";
}
