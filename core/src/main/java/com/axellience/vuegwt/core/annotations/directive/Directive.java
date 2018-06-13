package com.axellience.vuegwt.core.annotations.directive;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation placed on Vue Directives
 *
 * @author Adrien Baron
 */
@Target(TYPE)
@Retention(SOURCE)
public @interface Directive {

}
