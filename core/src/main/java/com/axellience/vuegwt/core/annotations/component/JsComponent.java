package com.axellience.vuegwt.core.annotations.component;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation placed on Vue Components
 *
 * @author Adrien Baron
 */
@Target(TYPE)
@Retention(SOURCE)
public @interface JsComponent {

  String value();

  String name() default "";
}