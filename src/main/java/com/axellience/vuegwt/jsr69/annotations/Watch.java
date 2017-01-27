package com.axellience.vuegwt.jsr69.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Annotation placed on Vue Components
 * @author Adrien Baron
 */
@Target(METHOD)
@Retention(SOURCE)
public @interface Watch
{
    String watchedProperty();
}
