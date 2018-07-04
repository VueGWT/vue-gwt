package com.axellience.vuegwt.core.annotations.component;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Mark a field as "Data" in a Component. This field will be observed recursively by Vue.js
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Data {

}
