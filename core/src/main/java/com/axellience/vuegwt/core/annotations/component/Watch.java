package com.axellience.vuegwt.core.annotations.component;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Mark a watcher for a property of the component data model
 * @author Adrien Baron
 */
@Target(METHOD)
@Retention(SOURCE)
public @interface Watch
{
    String propertyName();
    boolean isDeep() default false;
}
