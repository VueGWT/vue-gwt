package com.axellience.vuegwt.jsr69.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Mark a computed property
 * @author Adrien Baron
 */
@Target(METHOD)
@Retention(SOURCE)
public @interface Computed
{
    String propertyName() default "";
}
