package com.axellience.vuegwt.core.annotations.component;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Mark a field as a Ref to an element from the template
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Ref {

}
