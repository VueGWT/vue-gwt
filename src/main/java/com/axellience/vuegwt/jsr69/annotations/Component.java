package com.axellience.vuegwt.jsr69.annotations;

import com.axellience.vuegwt.client.VueComponent;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Annotation placed on Vue Components
 * @author Adrien Baron
 */
@Target(TYPE)
@Retention(SOURCE)
public @interface Component
{
    Class<? extends VueComponent>[] components() default {};
    String[] props() default {};
}
