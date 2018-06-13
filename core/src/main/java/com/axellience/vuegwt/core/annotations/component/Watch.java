package com.axellience.vuegwt.core.annotations.component;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Mark a watcher for a property of the component data model
 *
 * @author Adrien Baron
 */
@Target(METHOD)
@Retention(SOURCE)
public @interface Watch {

  String value();

  boolean isDeep() default false;
}
