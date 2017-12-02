package com.axellience.vuegwt.core.annotations.style;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Annotation placed on Style
 * Will be removed in Vue GWT beta-6.
 * Please check https://axellience.github.io/vue-gwt/gwt-integration/client-bundles-and-styles.html#styles
 * to see how to use styles in your templates without this annotation.
 * @author Adrien Baron
 */
@Target(TYPE)
@Retention(SOURCE)
@Deprecated
public @interface Style
{}