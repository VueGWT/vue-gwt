package com.axellience.vuegwt.jsr69.component.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Mark a Java property as being a Vue component Property passed from your Component parent
 * @author Adrien Baron
 */
@Target(METHOD)
@Retention(SOURCE)
public @interface PropDefault
{
    /**
     * Name of the property
     * @return the name of the property
     */
    String propertyName();
}
