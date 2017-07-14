package com.axellience.vuegwt.jsr69.component.annotations;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.directive.VueDirective;

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
    String name() default "";

    /**
     * Should use a factory for the data model.
     * Passing this to false will make all your components instance share the same data model.
     * @return true if we should use a factory, false otherwise
     */
    boolean useFactory() default true;

    /**
     * Components to register on this component instance
     * @return This list of {@link VueComponent} to register on this Component
     */
    Class<? extends VueComponent>[] components() default {};

    /**
     * Directives to register on this component instance
     * @return This list of {@link VueDirective} to register on this Component
     */
    Class<? extends VueDirective>[] directives() default {};
}