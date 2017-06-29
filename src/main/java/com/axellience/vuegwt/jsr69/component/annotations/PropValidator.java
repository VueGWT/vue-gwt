package com.axellience.vuegwt.jsr69.component.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Mark that the given method is used as a validator for the given component property
 * @author Adrien Baron
 */
@Target(METHOD)
@Retention(SOURCE)
public @interface PropValidator
{
    /**
     * Name of the property
     */
    String propertyName();
}
