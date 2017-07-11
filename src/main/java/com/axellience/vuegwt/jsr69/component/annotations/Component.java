package com.axellience.vuegwt.jsr69.component.annotations;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.VueDirective;

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
     * Should use a factory for the data model
     * Passing this to false will make all your components instance share the same data model
     */
    boolean useFactory() default true;

    /**
     * Components to register on this component instance
     */
    Class<? extends VueComponent>[] components() default {};

    /**
     * Directives to register on this component instance
     */
    Class<? extends VueDirective>[] directives() default {};
}