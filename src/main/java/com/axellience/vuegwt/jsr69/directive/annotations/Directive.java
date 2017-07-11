package com.axellience.vuegwt.jsr69.directive.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Annotation placed on Vue Directives
 * @author Adrien Baron
 */
@Target(TYPE)
@Retention(SOURCE)
public @interface Directive
{}
