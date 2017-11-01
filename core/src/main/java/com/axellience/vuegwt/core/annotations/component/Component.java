package com.axellience.vuegwt.core.annotations.component;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.client.component.options.CustomizeOptions;
import com.axellience.vuegwt.core.client.directive.VueDirective;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Annotation placed on Vue Components
 * @author Adrien Baron
 */
@Target(TYPE)
@Retention(CLASS)
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

    /**
     * Object responsible to customize the options of the Component.
     * This can be used to register routes with Vue router, or more.
     * They are injected if the component is injected.
     * @return This list of {@link CustomizeOptions}
     */
    Class<? extends CustomizeOptions>[] customizeOptions() default {};

    /**
     * A flag to set that the component doesn't have a template.
     * If the component is abstract, or implement HasRender then it's consider false by default.
     * @return true if has a html template, false otherwise
     */
    boolean hasTemplate() default true;
}