package com.axellience.vuegwt.core.annotations.component;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Method annotated with this will emit an event automatically when called Similar to @Emit() from
 * vue-property-decorator https://github.com/kaorun343/vue-property-decorator
 */
@Target(METHOD)
@Retention(CLASS)
public @interface Emit {

  String value() default "";
}
