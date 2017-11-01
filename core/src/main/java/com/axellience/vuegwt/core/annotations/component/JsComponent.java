package com.axellience.vuegwt.core.annotations.component;

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
public @interface JsComponent
{

}