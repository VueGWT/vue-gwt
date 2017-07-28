package com.axellience.vuegwt.jsr69.component.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Mark a Java property as being a Vue component Property passed from your Component parent
 * @author Adrien Baron
 */
@Target(FIELD)
@Retention(SOURCE)
public @interface Prop
{
    /**
     * Is the property required
     * @return true if the property is required, false otherwise. Default to false.
     */
    boolean required() default false;

    /**
     * Should check the type of the property.
     * By default check based on the Java type.
     * @return true if we should check the type, false otherwise. Default to false.
     */
    boolean checkType() default false;
}
